import pytest
from fastapi.testclient import TestClient
from app.config.settings import Settings
from app.main import create_app

@pytest.fixture
def client():
    with TestClient(create_app(Settings(database_url='sqlite://', eureka_enabled=False))) as c:
        yield c

P = {'voyageurId':'voyageur-1','destination':'Tunis','budgetMin':50,'budgetMax':300,'typeHebergement':'MaisonHote'}

def create_pref(c, **changes):
    response = c.post('/api/preferences', json={**P, **changes})
    assert response.status_code == 201, response.text
    return response.json()['idPreference']

def create_rec(c, pid, **changes):
    payload = {'idPreference':pid,'voyageurId':'voyageur-1','hebergementId':1,'scoreGlobal':0.85,**changes}
    response = c.post('/api/recommandations', json=payload)
    assert response.status_code == 201, response.text
    return response.json()['idRecommendation']

def test_preference_crud(client):
    pid = create_pref(client)
    assert client.get('/api/preferences').json()[0]['idPreference'] == pid
    assert client.get(f'/api/preferences/{pid}').json()['recommandationIds'] == []
    assert client.put(f'/api/preferences/{pid}', json={**P,'destination':'Sousse'}).json()['destination'] == 'Sousse'
    assert client.delete(f'/api/preferences/{pid}').status_code == 204
    assert client.get(f'/api/preferences/{pid}').status_code == 404

def test_recommandation_crud_relation(client):
    a, b = create_pref(client), create_pref(client)
    rid, rid2 = create_rec(client, a), create_rec(client, a)
    assert client.get(f'/api/preferences/{a}').json()['recommandationIds'] == [rid, rid2]
    assert client.get('/api/recommandations').status_code == 200
    assert client.get(f'/api/recommandations/{rid}').json()['idPreference'] == a
    r = client.put(f'/api/recommandations/{rid}', json={'idPreference':b,'voyageurId':'voyageur-1','hebergementId':2,'scoreGlobal':0.95})
    assert r.status_code == 200, r.text
    assert r.json()['idPreference'] == b
    assert client.get(f'/api/preferences/{a}').json()['recommandationIds'] == [rid2]
    assert client.get(f'/api/preferences/{b}').json()['recommandationIds'] == [rid]
    assert client.delete(f'/api/recommandations/{rid}').status_code == 204
    assert client.get(f'/api/recommandations/{rid}').status_code == 404
    assert client.get(f'/api/preferences/{b}').json()['recommandationIds'] == []

def test_errors(client):
    pid = create_pref(client)
    create_rec(client,pid)
    assert client.delete(f'/api/preferences/{pid}').status_code == 409
    assert client.put(f'/api/preferences/{pid}', json={**P,'voyageurId':'autre'}).status_code == 409
    body = {'idPreference':pid,'voyageurId':'autre','hebergementId':1,'scoreGlobal':0.85}
    assert client.post('/api/recommandations',json=body).status_code == 409
    assert client.post('/api/recommandations',json={**body,'idPreference':999}).status_code == 404
    invalid = client.post('/api/preferences',json={**P,'budgetMin':500})
    assert invalid.status_code == 400 and invalid.headers['content-type'].startswith('application/problem+json')
    assert client.get('/api/preferences/abc').status_code == 400
    assert client.post('/api/preferences',content='{').status_code == 400

def test_health_swagger(client):
    create_pref(client)
    r=client.get('/actuator/health')
    assert r.status_code == 200 and r.json()['status'] == 'UP'
    assert r.json()['components']['recommandation']['details']['nombrePreferences'] == 1
    assert client.get('/actuator/info').json()['app']['port'] == 8086
    assert client.get('/docs').status_code == 200
    schema=client.get('/openapi.json').json()
    assert '400' in schema['paths']['/api/preferences']['post']['responses']
