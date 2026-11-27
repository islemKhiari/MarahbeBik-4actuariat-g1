import asyncio
import httpx
from app.config.eureka_client import EurekaClient
from app.config.settings import Settings

def test_eureka_registration_renewal_and_deregistration():
    calls=[]
    async def scenario():
        stop=asyncio.Event()
        e=EurekaClient(Settings(port=8070))
        def handler(request):
            calls.append((request.method,request.url.path))
            if request.method=='POST':
                import json
                data=json.loads(request.content)['instance']
                assert data['port']['$']==8070 and data['instanceId'].endswith(':8070')
            if request.method=='PUT': stop.set()
            return httpx.Response(204 if request.method=='POST' else 200)
        async with httpx.AsyncClient(transport=httpx.MockTransport(handler)) as client:
            await e.register(client)
            assert e.registered
            await e.run(client,stop)
            await e.deregister(client)
            assert not e.registered
    asyncio.run(scenario())
    assert [method for method,_ in calls]==['POST','PUT','DELETE']
    assert calls[0][1]=='/eureka/apps/RECOMMANDATION'
    assert calls[1][1].endswith(':8070')

def test_two_instance_ids():
    assert EurekaClient(Settings(port=8070)).instance_id != EurekaClient(Settings(port=8071)).instance_id
