## 2019-01-28 v0.0.1-SNAPSHOT
* Initial module setup
* Implemented CRUD endpoints for Job Profile entity
* Populated sample Job Profile data
* Implemented CRUD endpoints for Match Profile entity

    | METHOD |             URL                          | DESCRIPTION                       |
    |--------|------------------------------------------|-----------------------------------|
    | GET    | /data-import-profiles/jobProfiles        | Get collection of Job Profiles    |
    | POST   | /data-import-profiles/jobProfiles        | Create Job Profile                |
    | GET    | /data-import-profiles/jobProfiles/{id}   | Get Job Profile by id             |
    | PUT    | /data-import-profiles/jobProfiles/{id}   | Update Job Profile entity         |
    | DELETE | /data-import-profiles/jobProfiles/{id}   | Delete Job Profile entity         |
    | GET    | /data-import-profiles/matchProfiles      | Get collection of Match Profiles  |
    | POST   | /data-import-profiles/matchProfiles      | Create Match Profile              |
    | GET    | /data-import-profiles/matchProfiles/{id} | Get Match Profile by id           |
    | PUT    | /data-import-profiles/matchProfiles/{id} | Update Match Profile entity       |
    | DELETE | /data-import-profiles/matchProfiles/{id} | Delete Match Profile entity       |
