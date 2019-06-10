## 2019-05-17 v1.3.0-SNAPSHOT
* Updated association profiles samples. 
* Updated API endpoint for getting details by master id. Added query param to specify return type. Added searching by cql query for details
* Updated API endpoint for getting masters by detail id. Added query param to specify return type. Added searching by cql query for details
* Updated API endpoints for Profile Association entity. Added query parameters to specify master and detail types of profile association.
* Updated Action Profile sample data.

  | METHOD |             URL                                              | DESCRIPTION                         |
  |--------|--------------------------------------------------------------|-------------------------------------|
  | GET    | /data-import-profiles/jobProfiles                            | Get collection of Job Profiles      |
  | POST   | /data-import-profiles/jobProfiles                            | Create Job Profile                  |
  | GET    | /data-import-profiles/jobProfiles/{id}                       | Get Job Profile by id               |
  | PUT    | /data-import-profiles/jobProfiles/{id}                       | Update Job Profile entity           |
  | DELETE | /data-import-profiles/jobProfiles/{id}                       | Delete Job Profile entity           |
  | GET    | /data-import-profiles/matchProfiles                          | Get collection of Match Profiles    |
  | POST   | /data-import-profiles/matchProfiles                          | Create Match Profile                |
  | GET    | /data-import-profiles/matchProfiles/{id}                     | Get Match Profile by id             |
  | PUT    | /data-import-profiles/matchProfiles/{id}                     | Update Match Profile entity         |
  | DELETE | /data-import-profiles/matchProfiles/{id}                     | Delete Match Profile entity         |
  | GET    | /data-import-profiles/actionProfiles                         | Get collection of Action Profiles   |
  | POST   | /data-import-profiles/actionProfiles                         | Create Action Profile               |
  | GET    | /data-import-profiles/actionProfiles/{id}                    | Get Action Profile by id            |
  | PUT    | /data-import-profiles/actionProfiles/{id}                    | Update Action Profile entity        |
  | DELETE | /data-import-profiles/actionProfiles/{id}                    | Delete Action Profile entity        |
  | GET    | /data-import-profiles/mappingProfiles                        | Get collection of Mapping Profiles  |
  | POST   | /data-import-profiles/mappingProfiles                        | Create Mapping Profile              |
  | GET    | /data-import-profiles/mappingProfiles/{id}                   | Get Mapping Profile by id           |
  | PUT    | /data-import-profiles/mappingProfiles/{id}                   | Update Mapping Profile entity       |
  | DELETE | /data-import-profiles/mappingProfiles/{id}                   | Delete Mapping Profile entity       | 
  | POST   | /data-import-profiles/profileAssociations?master={masterType}&detail={detailType}                    | Create Profile Association with appropriate masterType and detailType               |
  | GET    | /data-import-profiles/profileAssociations?master={masterType}&detail={detailType}                    | Get all Profile Association , masterType and detailType                             |
  | GET    | /data-import-profiles/profileAssociations/{id}?master={masterType}&detail={detailType}               | Get Profile Association with appropriate masterType and detailType                  |
  | PUT    | /data-import-profiles/profileAssociations/{id}?master={masterType}&detail={detailType}               | Update Profile Association entity by id with appropriate masterType and detailType  |
  | DELETE | /data-import-profiles/profileAssociations/{id}?master={masterType}&detail={detailType}               | Delete Profile Association entity by id , masterType and detailType                 |
  | GET    | /data-import-profiles/jobProfileSnapshots/{id}               | Get Job Profile Snapshot entity     |
  | POST   | /data-import-profiles/jobProfileSnapshots/{id}               | Create Job Profile Snapshot entity  |
  | GET    | /data-import-profiles/profileAssociations/{masterId}/details?masterType={masterType}| Get details by master profile masterType: JOB_PROFILE, ACTION_PROFILE, MATCH_PROFILE, MAPPING_PROFILE |
  | GET    | /data-import-profiles/profileAssociations/{detailId}/masters?detailType={detailType}| Get masters by detail profile detailType: JOB_PROFILE, ACTION_PROFILE, MATCH_PROFILE, MAPPING_PROFILE |
  | GET    | /data-import-profiles/entityTypes                            | Get collection of entity types      |

## 2019-05-16 v1.2.0
* Changed delete methods logic for action, job, match and mapping profiles. Profiles are being marked as deleted and not removed permanently from database.
* Fixed get API endpoints for jobProfiles and matchProfiles when sortedBy is specified.

## 2019-05-10 v1.1.2
* Added default Job Profile for MARC Bibs records import
* Added Match Profile samples

## 2019-05-03 v1.1.0
* Added endpoint to receive entity types list.
* Updated Job Profile schema to contain single dataType. 
* Updated existing sample data.
* Added methods to work with associated profiles.
* Designed and created Job Profile Snapshot entity.
* Designed and created Job Profile Snapshot database view.
* Added unique constraint for Job Profile name.
* Updated Tenant API version.
* Populated sample Match Profile data.

  | METHOD |             URL                                              | DESCRIPTION                         |
  |--------|--------------------------------------------------------------|-------------------------------------|
  | GET    | /data-import-profiles/jobProfiles                            | Get collection of Job Profiles      |
  | POST   | /data-import-profiles/jobProfiles                            | Create Job Profile                  |
  | GET    | /data-import-profiles/jobProfiles/{id}                       | Get Job Profile by id               |
  | PUT    | /data-import-profiles/jobProfiles/{id}                       | Update Job Profile entity           |
  | DELETE | /data-import-profiles/jobProfiles/{id}                       | Delete Job Profile entity           |
  | GET    | /data-import-profiles/matchProfiles                          | Get collection of Match Profiles    |
  | POST   | /data-import-profiles/matchProfiles                          | Create Match Profile                |
  | GET    | /data-import-profiles/matchProfiles/{id}                     | Get Match Profile by id             |
  | PUT    | /data-import-profiles/matchProfiles/{id}                     | Update Match Profile entity         |
  | DELETE | /data-import-profiles/matchProfiles/{id}                     | Delete Match Profile entity         |
  | GET    | /data-import-profiles/actionProfiles                         | Get collection of Action Profiles   |
  | POST   | /data-import-profiles/actionProfiles                         | Create Action Profile               |
  | GET    | /data-import-profiles/actionProfiles/{id}                    | Get Action Profile by id            |
  | PUT    | /data-import-profiles/actionProfiles/{id}                    | Update Action Profile entity        |
  | DELETE | /data-import-profiles/actionProfiles/{id}                    | Delete Action Profile entity        |
  | GET    | /data-import-profiles/mappingProfiles                        | Get collection of Mapping Profiles  |
  | POST   | /data-import-profiles/mappingProfiles                        | Create Mapping Profile              |
  | GET    | /data-import-profiles/mappingProfiles/{id}                   | Get Mapping Profile by id           |
  | PUT    | /data-import-profiles/mappingProfiles/{id}                   | Update Mapping Profile entity       |
  | DELETE | /data-import-profiles/mappingProfiles/{id}                   | Delete Mapping Profile entity       |
  | POST   | /data-import-profiles/profileAssociations                    | Create Profile Association          |
  | GET    | /data-import-profiles/profileAssociations/{id}               | Get Profile Association by id       |
  | PUT    | /data-import-profiles/profileAssociations/{id}               | Update Profile Association entity   |
  | DELETE | /data-import-profiles/profileAssociations/{id}               | Delete Profile Association entity   |
  | GET    | /data-import-profiles/jobProfileSnapshots/{id}               | Get Job Profile Snapshot entity     |
  | POST   | /data-import-profiles/jobProfileSnapshots/{id}               | Create Job Profile Snapshot entity  |
  | GET    | /data-import-profiles/profileAssociations/{masterId}/details?masterType={masterType}| Get details by master profile masterType: JOB_PROFILE, ACTION_PROFILE, MATCH_PROFILE, MAPPING_PROFILE |
  | GET    | /data-import-profiles/profileAssociations/{detailId}/masters?detailType={detailType}| Get masters by detail profile detailType: JOB_PROFILE, ACTION_PROFILE, MATCH_PROFILE, MAPPING_PROFILE |
  | GET    | /data-import-profiles/entityTypes                            | Get collection of entity types      |


## 2019-03-20 v1.0.0
* Initial module setup
* Implemented CRUD endpoints for Job Profile entity
* Populated sample Job Profile data
* Implemented CRUD endpoints for Match Profile entity
* Implemented CRUD endpoints for Mapping Profile entity
* Implemented CRUD endpoints for Action Profile entity
* Implemented CRUD endpoint for Profile Associations
* Implemented GET and POST methods for Job Profile Snapshot
* Implemented GET method for retrieving detail profiles by master profile id
* Implemented GET method for retrieving master profiles by detail profile id

  | METHOD |             URL                                              | DESCRIPTION                         |
  |--------|--------------------------------------------------------------|-------------------------------------|
  | GET    | /data-import-profiles/jobProfiles                            | Get collection of Job Profiles      |
  | POST   | /data-import-profiles/jobProfiles                            | Create Job Profile                  |
  | GET    | /data-import-profiles/jobProfiles/{id}                       | Get Job Profile by id               |
  | PUT    | /data-import-profiles/jobProfiles/{id}                       | Update Job Profile entity           |
  | DELETE | /data-import-profiles/jobProfiles/{id}                       | Delete Job Profile entity           |
  | GET    | /data-import-profiles/matchProfiles                          | Get collection of Match Profiles    |
  | POST   | /data-import-profiles/matchProfiles                          | Create Match Profile                |
  | GET    | /data-import-profiles/matchProfiles/{id}                     | Get Match Profile by id             |
  | PUT    | /data-import-profiles/matchProfiles/{id}                     | Update Match Profile entity         |
  | DELETE | /data-import-profiles/matchProfiles/{id}                     | Delete Match Profile entity         |
  | GET    | /data-import-profiles/actionProfiles                         | Get collection of Action Profiles   |
  | POST   | /data-import-profiles/actionProfiles                         | Create Action Profile               |
  | GET    | /data-import-profiles/actionProfiles/{id}                    | Get Action Profile by id            |
  | PUT    | /data-import-profiles/actionProfiles/{id}                    | Update Action Profile entity        |
  | DELETE | /data-import-profiles/actionProfiles/{id}                    | Delete Action Profile entity        |
  | GET    | /data-import-profiles/mappingProfiles                        | Get collection of Mapping Profiles  |
  | POST   | /data-import-profiles/mappingProfiles                        | Create Mapping Profile              |
  | GET    | /data-import-profiles/mappingProfiles/{id}                   | Get Mapping Profile by id           |
  | PUT    | /data-import-profiles/mappingProfiles/{id}                   | Update Mapping Profile entity       |
  | DELETE | /data-import-profiles/mappingProfiles/{id}                   | Delete Mapping Profile entity       |
  | POST   | /data-import-profiles/profileAssociations                    | Create Profile Association          |
  | GET    | /data-import-profiles/profileAssociations/{id}               | Get Profile Association by id       |
  | PUT    | /data-import-profiles/profileAssociations/{id}               | Update Profile Association entity   |
  | DELETE | /data-import-profiles/profileAssociations/{id}               | Delete Profile Association entity   |
  | GET    | /data-import-profiles/jobProfileSnapshots/{id}               | Get Job Profile Snapshot entity     |
  | POST   | /data-import-profiles/jobProfileSnapshots/{id}               | Create Job Profile Snapshot entity  |
  | GET    | /data-import-profiles/profileAssociations/{masterId}/details?masterType={masterType}| Get details by master profile masterType: JOB_PROFILE, ACTION_PROFILE, MATCH_PROFILE, MAPPING_PROFILE |
  | GET    | /data-import-profiles/profileAssociations/{detailId}/masters?detailType={detailType}| Get masters by detail profile detailType: JOB_PROFILE, ACTION_PROFILE, MATCH_PROFILE, MAPPING_PROFILE |
        
