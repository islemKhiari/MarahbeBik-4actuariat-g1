import asyncio
import logging
from urllib.parse import quote
import httpx

logger = logging.getLogger(__name__)

class EurekaClient:
    """Client REST Eureka: inscription, renouvellement du bail et desinscription."""
    def __init__(self, settings):
        self.settings = settings
        self.registered = False
        self.app = settings.app_name.upper()
        self.instance_id = f"{settings.host}:{self.app}:{settings.port}"
        self.app_url = settings.eureka_url.rstrip('/') + '/apps/' + quote(self.app, safe='')
        self.instance_url = self.app_url + '/' + quote(self.instance_id, safe='')
        self.base_url = f"http://{settings.host}:{settings.port}"

    async def register(self, client):
        payload = {"instance": {
            "instanceId": self.instance_id, "hostName": self.settings.host,
            "app": self.app, "ipAddr": self.settings.host, "status": "UP",
            "port": {"$": self.settings.port, "@enabled": "true"},
            "securePort": {"$": 443, "@enabled": "false"},
            "dataCenterInfo": {"@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo", "name": "MyOwn"},
            "vipAddress": self.settings.app_name.lower(), "secureVipAddress": self.settings.app_name.lower(),
            "homePageUrl": self.base_url + '/', "statusPageUrl": self.base_url + '/actuator/info',
            "healthCheckUrl": self.base_url + '/actuator/health',
            "leaseInfo": {"renewalIntervalInSecs": 30, "durationInSecs": 90},
            "metadata": {"management.port": str(self.settings.port)}
        }}
        response = await client.post(self.app_url, json=payload)
        response.raise_for_status()
        self.registered = True
        logger.info("Eureka: instance enregistree %s", self.instance_id)

    async def run(self, client, stop):
        while not stop.is_set():
            try:
                if not self.registered:
                    await self.register(client)
                else:
                    response = await client.put(self.instance_url)
                    if response.status_code == 404:
                        self.registered = False
                        await self.register(client)
                    else:
                        response.raise_for_status()
            except httpx.HTTPError as exc:
                self.registered = False
                logger.warning("Eureka inaccessible: %s", exc)
            try:
                await asyncio.wait_for(stop.wait(), timeout=30 if self.registered else 5)
            except TimeoutError:
                pass

    async def deregister(self, client):
        try:
            response = await client.delete(self.instance_url)
            if response.status_code != 404:
                response.raise_for_status()
        except httpx.HTTPError as exc:
            logger.warning("Eureka: desinscription non confirmee: %s", exc)
        finally:
            self.registered = False
