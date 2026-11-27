# MS5 — Recommandation intelligente (Python)

Microservice du projet MarhbaBiik. FastAPI + SQLAlchemy 2 + Pydantic + PostgreSQL. Le serveur Eureka Java existant (port 8761) ne change pas.

## Architecture

- `app/entity` : les deux modèles ORM et leur association bidirectionnelle 1:N (`back_populates`).
- `app/dto` : DTO Request et Response, validation des champs (budget, identifiants, etc.).
- `app/mapper` : conversion DTO ↔ entité.
- `app/repository` : accès aux données via ORM, sans SQL écrit à la main pour le CRUD.
- `app/service` : CRUD, transactions, cohérence du voyageur et association.
- `app/controller` : les dix endpoints CRUD.
- `app/exception` : erreurs HTTP 400/404/409 au format Problem Details.
- `app/config` : paramètres, base de données, Swagger et client REST Eureka.
- `app/health` : endpoints compatibles avec les chemins habituels Actuator, mais implémentés en Python.
- `tests` : tests automatisés SQLite et client Eureka simulé.

`PreferenceVoyageur` possède 0..* recommandations ; une recommandation appartient à une seule préférence. La FK locale `idPreference` et les identifiants de recommandations dans le DTO rendent cette relation visible. `hebergementId` et `voyageurId` sont des références externes, sans FK vers les bases des autres microservices. L’existence de l’hébergement et du voyageur dans les autres services n’est pas vérifiée ici.

Le score est saisi dans le CRUD : aucun algorithme IA ou calcul de score n'est encore défini. `dateGeneration` est générée côté serveur en UTC à la création. Le format numérique du budget est décimal (les réponses JSON peuvent le représenter comme une chaîne). La suppression d'une préférence ayant des recommandations est refusée (409) et le voyageur ne peut pas être modifié tant que la préférence a des recommandations.

## 1. Installer sur Windows

Copier le dossier `Backend/microservices/Recommandation` dans le dépôt existant (ne pas remplacer les autres microservices). Ouvrir le terminal PowerShell dans ce dossier.

```powershell
py -3 -m venv .venv
.\.venv\Scripts\python.exe -m pip install -r requirements-dev.txt
Copy-Item .env.example .env
```

Python 3.11 ou plus récent recommandé. Ne pas committer `.env` ni `.venv`. Si le mot de passe PostgreSQL contient des caractères spéciaux, les encoder dans l'URL de connexion.

Démarrer PostgreSQL (port 5432 par défaut). Dans pgAdmin 4 (Query Tool de la base postgres), exécuter `init_db.sql` :

```sql
CREATE DATABASE recommandation_db;
```

Si vous aviez testé une version MySQL, arrêter cette version, réinstaller les dépendances avec `requirements-dev.txt` et mettre à jour votre `.env` existant (ne pas le conserver avec une URL MySQL). Les données MySQL ne sont pas transférées automatiquement.

Modifier `.env` selon vos identifiants PostgreSQL et le port choisi. PostgreSQL est la base exigée pour MS5. Remplacer VOTRE_MOT_DE_PASSE par votre mot de passe PostgreSQL. Ne pas recréer la base si elle existe déjà. Les tables sont créées au démarrage par SQLAlchemy (`create_all`, qui ne migre pas un schéma existant).

## 2. Démarrage

Démarrer PostgreSQL et Eureka Server Java (http://localhost:8761). Ensuite, dans le dossier Recommandation :

```powershell
.\.venv\Scripts\python.exe run.py
```

Liens :
- Swagger : http://localhost:8086/docs
- OpenAPI JSON : http://localhost:8086/openapi.json
- Health : http://localhost:8086/actuator/health
- Info : http://localhost:8086/actuator/info
- Eureka : http://localhost:8761 (application RECOMMANDATION)

Le client Eureka enregistre l'instance, renouvelle son bail toutes les 30 secondes et se désinscrit lors d'un arrêt normal. En cas d'échec, il réessaie. Si Eureka est activé mais indisponible, Health renvoie DOWN/503. L'API CRUD reste utilisable si la base fonctionne. Pour un test indépendant d'Eureka, mettre EUREKA_ENABLED=false dans .env.

## 3. Deux instances (8070 et 8071)

Arrêter l'instance 8086 si vous voulez exactement deux instances. Ouvrir deux terminaux PowerShell séparés, toujours dans le dossier Recommandation.

Terminal 1 :

```powershell
$env:SERVER_PORT="8070"
.\.venv\Scripts\python.exe run.py
```

Terminal 2 :

```powershell
$env:SERVER_PORT="8071"
.\.venv\Scripts\python.exe run.py
```

Les deux instances partagent la même base et le même nom de service, mais ont des IDs Eureka différents. Les ports 8070 et 8071 sont les ports retenus pour les deux instances, distincts des ports 8093/8094 du MS3. Tester http://localhost:8070/docs et http://localhost:8071/docs, puis /actuator/health sur chacun. Dans PyCharm (ou IntelliJ avec support Python), créer deux configurations Python avec `run.py`, le même dossier de travail et l'interpréteur `.venv`, puis indiquer `SERVER_PORT=8070` ou `SERVER_PORT=8071` dans Environment variables. Les VM options Java ne s'appliquent pas à Python.

## 4. CRUD et tests Swagger

| Opération | Préférences | Recommandations | HTTP |
|---|---|---|---|
| POST | /api/preferences | /api/recommandations | 201 |
| GET | /api/preferences | /api/recommandations | 200 |
| GET | /api/preferences/{identifiant} | /api/recommandations/{identifiant} | 200 |
| PUT | /api/preferences/{identifiant} | /api/recommandations/{identifiant} | 200 |
| DELETE | /api/preferences/{identifiant} | /api/recommandations/{identifiant} | 204 |

POST /api/preferences :

```json
{
  "voyageurId": "voyageur-1",
  "destination": "Tunis",
  "budgetMin": 50,
  "budgetMax": 300,
  "typeHebergement": "MaisonHote"
}
```

POST /api/recommandations (remplacer idPreference par la valeur générée) :

```json
{
  "idPreference": 1,
  "voyageurId": "voyageur-1",
  "hebergementId": 1,
  "scoreGlobal": 0.85
}
```

- GET préférence : vérifier `recommandationIds`.
- GET recommandation : vérifier `idPreference`.
- PUT préférence : modifier destination et reprendre tous les autres champs.
- PUT recommandation : modifier le score ou l'identifiant de préférence (même voyageur).
- GET ID inexistant : 404.
- POST budgetMin supérieur à budgetMax : 400.
- DELETE préférence ayant une recommandation : 409.
- DELETE recommandation : 204, puis GET préférence : liste mise à jour.
- DELETE préférence sans recommandation : 204.
- Créer avec le port 8070 et lire avec le port 8071 pour vérifier le partage de la base.

## 5. Tests automatisés avant le push

```powershell
.\.venv\Scripts\python.exe -m pytest -q
```

Les tests utilisent SQLite en mémoire et un serveur Eureka simulé (httpx MockTransport). Ils ne nécessitent pas PostgreSQL ni Eureka. Tester aussi la vraie base PostgreSQL et le vrai Eureka avant de pousser. Les tests ne valident pas le routage Gateway, le calcul automatique de recommandations, l'authentification Keycloak ni les communications interservices.

## 6. Git

Depuis la racine du dépôt :

```bash
git status
git add Backend/microservices/Recommandation/
git diff --cached --stat
git commit -m "feat(recommandation): implement Python CRUD, bidirectional mapping, Eureka and health endpoints"
git push
```

Vérifier la branche et ne pas inclure les fichiers des autres microservices. Aucun push n'est effectué par ce livrable.
