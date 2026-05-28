# Diacono Backend Project - Comprehensive Analysis

## Executive Summary
The Diacono Backend is a Spring Boot application built with **Clean Architecture** principles using the **UseCase pattern**. It's designed to manage church membership, ministries, events, and scheduling systems.

---

## 1. ARCHITECTURE OVERVIEW

### Design Pattern
- **Primary Pattern**: Clean Architecture with UseCase (Commands) pattern
- **Framework**: Spring Boot with Spring Data JPA
- **Build Tool**: Maven
- **Authentication**: Spring Security resource server with JWT and Google OAuth authorization-code login
- **API Documentation**: Swagger/OpenAPI 3.0

### Layered Structure
```
┌─────────────────────────────────────┐
│  Infrastructure Layer               │
│  (Controllers, Persistence, Auth)   │
├─────────────────────────────────────┤
│  UseCase Layer                      │
│  (Application Business Logic)       │
├─────────────────────────────────────┤
│  Domain Layer                       │
│  (Entities, Repositories, Services) │
├─────────────────────────────────────┤
│  Global/Cross-cutting Concerns      │
│  (Config, Error handling, Utils)    │
└─────────────────────────────────────┘
```

---

## 2. PACKAGE STRUCTURE & PURPOSE

### Root Package
- **Path**: `com.diacono.diacono`
- **Main Class**: `DiaconoApplication.java` - Spring Boot entry point with `@ConfigurationPropertiesScan`

### 2.1 Applications Layer (`applications/`)
**Purpose**: DTOs and mappers for API communication

#### Subdirectories:
- **dtos/** - Data Transfer Objects organized by domain
- **mappers/** - Mapping utilities for entity-to-DTO conversion

---

## 3. ALL PACKAGES AND SUBPACKAGES

### 3.1 Auth Package (`auth/`)
Authentication and authorization logic

| Subpackage | Purpose |
|-----------|---------|
| `handler/` | OAuth2 authentication handlers |
| `model/` | Custom OAuth2 user models |
| `service/` | Authentication services and utilities |

**Key Classes**:
- `CustomOAuth2AuthenticationSuccessHandler` - Handles successful OAuth2 authentication
- `CustomMembroOAuth2User` - Custom principal for authenticated members
- `CustomOidcUserService` - OpenID Connect user service

### 3.2 Domain Package (`domain/`)
Core business entities and domain logic

| Subpackage | Purpose |
|-----------|---------|
| `entity/` | JPA entities (data models) |
| `enums/` | Domain enumerations |
| `repository/` | Repository interfaces |
| `service/` | Domain services (business rules) |

#### Domain Entities (`domain/entity/`)
- **Membro** - Church member
- **Ministerio** - Church ministry/department
- **Evento** - Church event
- **EscalaEvento** - Event schedule
- **EscalaMinisterio** - Ministry member schedule
- **MembroMinisterio** - Member-ministry relationship
- **EnderecoMembro**, **EnderecoIgreja**, **EnderecoEvento** - Address entities
- **Recorrencia** - Event recurrence configuration
- **Igreja** - Church entity
- **GoogleRefreshTokenMembro** - OAuth2 token storage

#### Domain Enums (`domain/enums/`)
- `EnumCargoMembro` - Member roles
- `EnumCargoMembroMinisterio` - Ministry-specific member roles
- `EnumGeneroMembro` - Gender (M/F/Other)
- `EnumStatusEscalaMinisterio` - Ministry scale status
- `EnumStatusEvento` - Event status
- `EnumStatusMembro` - Member status
- `EnumStatusMinisterio` - Ministry status
- `TipoRecorrencia` - Recurrence type

#### Domain Repositories (`domain/repository/`)
Domain repository contracts:
- `EnderecoEventoRepository`
- `EscalaEventoRepository`
- `EscalaMinisterioRepository`
- `EventoRepository`
- `GoogleRefreshTokenMembroRepository`
- `IgrejaRepository`
- `MembroMinisterioRepository`
- `MembroRepository`
- `MinisteriosRepository`
- `RecorrenciaRepository`

#### Domain Services (`domain/service/`)
- `EscalaStatusDomainService` - Interface for scale status business logic
- `EscalaStatusDomainServiceImpl` - Implementation

### 3.3 Infrastructure Package (`infrastructure/`)
Technical implementation details and external system integration

#### Controllers (`infrastructure/controllers/`)
REST API endpoints:
- **MembroController** - Member operations (create, list, detail, update)
- **MinisteriosController** - Ministry operations (create, edit, list, member management)
- **EventoController** - Event operations (create, update, delete, search by month/year)
- **EscalaEventoController** - Event schedule management
- **EscalaMinisterioController** - Ministry schedule management
- **DashboardController** - Analytics and KPIs
- **GoogleAuthController** - Google OAuth integration
- **LoginController** - Login/authentication endpoints
- **CadastroController** - External registration/signup
- **PerfilController** - Authenticated member profile operations

#### Persistence (`infrastructure/persistence/`)
JPA implementations and query results:

**Membro Persistence**:
- `MembroJpaRepository` - Spring Data JPA interface
- `MembroRepositoryImpl` - Custom implementation

**Query Results** (specialized DTOs for complex queries):
- `EscalaEventoEscaladoQueryResult`
- `EscalaEventoQueryResult`
- `EscalaMembroMinisterioQueryResult`
- `EscalaMembroMinisterioSimplificadoQueryResult`
- `EscalaMinisterioConsolidadoQueryResult`
- `EscalaMinisterioQueryResult`

Observação: a consulta de ministérios do membro usa projeção direta para `MinisterioSuperSimplificadoDTO`.

#### Messaging (`infrastructure/messaging/`)
- `EventoProducer` - Publishes created event messages after transaction commit

**Other Persistence Packages**:
- `gateway/` - Domain repository implementations backed by Spring Data repositories
- `springdata/` - Spring Data JPA repositories
- `projection/` - Read-only projection interfaces for complex queries

#### Exceptions (`infrastructure/exceptions/`)
Custom exception classes:
- `DateInvalidException` - Invalid date input
- `TimeInvalidException` - Invalid time input
- `MembroNaoEncontradoException` - Member not found
- `MinisterioNaoEncontradoException` - Ministry not found

#### Auth Integration (`infrastructure/auth/`)
Google OAuth2 integration:
- `GoogleAuthorizationCodeExchanger` - Interface for authorization-code exchange
- `GoogleAuthorizationCodeExchangerImpl` - Exchanges Google authorization code for tokens
- `GoogleIdTokenVerifier` - Interface for token verification
- `GoogleIdTokenVerifierImpl` - Google token validation implementation

### 3.4 UseCase Package (`usecases/`)
Application-specific business logic organized by domain

#### Root UseCase Classes
- `BuscarPerfilUseCase` - Authenticated member profile retrieval
- `GenerateTokenUseCase` - JWT token generation
- `LoginServiceUseCase` - Login business logic

#### Events UseCase (`usecases/eventos/`)
Event management operations:
- `CriarEventoUseCase` - Create event with recurrence
- `BuscarEventosPorMesEAnoUseCase` - Search events by month/year
- `BuscarEventoEspecificoUseCase` - Get specific event details
- `BuscarEnderecoEventoPorUUIDUseCase` - Search event addresses by UUID
- `BuscarEnderecoEventoUseCase` - Get all event addresses
- `AtualizarEventoUseCase` - Update event
- `ApagarEventoUnicoUseCase` - Delete single event
- `ApagarEventosMultiplosUseCase` - Delete multiple events
- `BuscarMinisterioPorUUIDUseCase` - Get ministry by ID for event workflows
- **validation/** - Event validation logic

#### Ministry UseCase (`usecases/ministerio/`)
Ministry and member-ministry operations:
- `AdicionarMinisterioUseCase` - Create new ministry
- `EditarMinisterioUseCase` - Update ministry details
- `BuscarMinisteriosGeraisUseCase` - Get all active ministries
- `BuscarMinisteriosGovernoSemFiltroUseCase` - Admin view without filters
- `BuscarMinisteriosGovernoComFiltroUseCase` - Admin view with search/status filters
- `BuscarMinisteriosLiderMinisterioUseCase` - Ministry leader's ministries
- `BuscarMinisteriosMembroUseCase` - Authenticated member's ministries
- `BuscarMembroMinisterioLiderMinisterioSemFiltroUseCase` - List ministry members
- `BuscarMembroMinisterioLiderMinisterioComFiltroUseCase` - Filter ministry members
- `AdicionarMembroMinisterioLiderMinisterioUseCase` - Add member to ministry
- `RemoverMembroMinisterioLiderMinisterioUseCase` - Remove member from ministry

#### Member UseCase (`usecases/membro/`)
Member operations:
- `CriarMembroUseCase` - Create new member
- `CadastrarMembroUseCase` - Register via external form
- `AtualizarMembroUseCase` - Update member and ministry links
- `BuscarMembroPorUUIDUseCase` - Get member details by ID
- `BuscarTodosSemFiltroUseCase` - List all members paginated
- `BuscarTodosComFiltroUseCase` - Search with filters (name, status, ministry)
- `BuscarPorEmaiUseCase` - Find member by email
- **validation/** - Member validation

#### Event Scale UseCase (`usecases/escalasevento/`)
Event schedule generation and management:
- `GerarEscalaEventoUseCase` - Generate event schedule
- `AtualizarEscalaEventoPorEventoIdUseCase` - Update event schedule
- `BuscarEscalaEventoConsolidadoPorMesAnoUseCase` - Consolidated view by month/year
- `BuscarEscalaEventoEscaladoPorEventoIdUseCase` - Get event schedule details
- **validation/** - Schedule validation

#### Ministry Scale UseCase (`usecases/escalasministerio/`)
Ministry member scheduling:
- `SalvarEscalaMinisterioPorEscalaEventoIdUseCase` - Assign members to event
- `BuscarEscalaMinisterioConsolidadoPorMesAnoUseCase` - Ministry schedule overview
- `BuscarEscalaMinisterioPorMembroIdMesAnoUseCase` - Individual member schedule
- `BuscarMembrosMinisterioPorEscalaEventoIdUseCase` - Members in event schedule
- `BuscarMembrosMinisterioDisponiveisPorEscalaEventoIdUseCase` - Available members
- `BuscarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase` - Auto-assign members
- `RevisarMembrosMinisterioRandomizadosPorEscalaEventoIdUseCase` - Review assignments

#### Dashboard UseCase (`usecases/dashboard/`)
Analytics and KPI generation

**Members Analytics** (`dashboard/membros/`):
- `BuscarKpiEvolucaoMembrosDashUseCase` - Member growth over time
- `BuscarKpiFaixaEtariaMembrosDashUseCase` - Members by age group
- `BuscarKpiGeneroMembrosDashUseCase` - Members by gender
- `BuscarKpiMembrosDashUseCase` - General member KPIs

**Ministry Analytics** (`dashboard/ministerios/`):
- `BuscarKpiEvolucaoMinisteriosDashUseCase` - Ministry growth
- `BuscarKpiMinisteriosDashUseCase` - General ministry KPIs
- `BuscarKpiQuantidadeMembrosPorMinisterioDashUseCase` - Members per ministry
- `buscarKpiQuantidadeEventosPorMinisterioDashUseCase` - Events per ministry

#### Church UseCase (`usecases/igreja/`)
Church information:
- `BuscasIgrejasUseCase` - List churches
- `BuscarIgrejaPorUUIDUseCase` - Get church by ID

#### Google Auth UseCase (`usecases/googleauth/`)
OAuth2 integrations:
- `LoginGoogleUseCase` - Google authorization-code login orchestration
- `AutenticarGoogleUseCase` - Google ID token validation
- `AtualizarSecretGoogleUseCase` - Update Google OAuth secrets

### 3.5 Global Package (`global/`)
Cross-cutting concerns

#### Configuration (`global/config/`)
- `GoogleOAuthProperties` - Google OAuth configuration properties
- `SecurityConfig` - Spring Security configuration (OAuth2, JWT)
- `RabbitMQConfig` - RabbitMQ exchange, queue and binding configuration for events
- `SensitiveDataStartupRunner` - Updates sensitive-field search indexes at startup
- `SensitiveFieldCryptoConfiguration` - Sensitive-field crypto configuration

#### Error Handling (`global/error/`)
- **exceptions/** - Custom exception hierarchy
- **comuns/** - Common error response handling
- `RestErrorMessage` - Error response DTO

#### Utilities (`global/util/`)
- `JwtUtils` - JWT claim extraction from the authenticated `SecurityContext`
- `IdEntityUtils` - UUID/ID utilities
- `SensitiveFieldCryptoUtils` - Encryption/decryption helper for sensitive fields
- `SensitiveSearchIndexUtils` - Deterministic hash helper for searchable sensitive fields
- `SensitiveStringAttributeConverter` - JPA converter for encrypted string fields

---

## 4. CONTROLLERS & ENDPOINTS

### MembroController
**Base Path**: `/api/v1/membros`

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/v1/membros` | Create new member |
| GET | `/api/v1/membros` | List members (with optional filters: search, status, ministry) |
| GET | `/api/v1/membros/{idExterno}` | Get member details |
| PATCH | `/api/v1/membros/{idExterno}` | Update member |

### MinisteriosController
**Base Path**: `/api/v1/ministerios`

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/v1/ministerios` | Get all active ministries |
| GET | `/api/v1/ministerios/governo` | Admin view of ministries (with filters) |
| POST | `/api/v1/ministerios/governo` | Create ministry |
| PATCH | `/api/v1/ministerios/governo/{idMinisterio}` | Update ministry |
| GET | `/api/v1/ministerios/lider-ministerio` | Ministry leader's ministries |
| GET | `/api/v1/ministerios/lider-ministerio/{idMinisterio}` | Members of specific ministry |
| GET | `/api/v1/ministerios/membro` | Authenticated member's ministries |
| PATCH | `/api/v1/ministerios/lider-ministerio/{idMinisterio}` | Add member to ministry |
| DELETE | `/api/v1/ministerios/lider-ministerio/{idMinisterio}/{idMembroMinisterio}` | Remove member from ministry |

### EventoController
**Base Path**: `/api/v1/eventos`

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/v1/eventos` | Get events by month/year |
| GET | `/api/v1/eventos/{id}` | Get specific event details |
| GET | `/api/v1/eventos/enderecos` | Get all event addresses |
| POST | `/api/v1/eventos` | Create event |
| PATCH | `/api/v1/eventos/{id}` | Update event |
| DELETE | `/api/v1/eventos/unico/{id}` | Delete single event |
| DELETE | `/api/v1/eventos/multiplos/{id}` | Delete recurring events from the selected event forward |

### EscalaEventoController
**Base Path**: `/api/v1/escalas-evento/governo`

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/v1/escalas-evento/governo` | Consolidated event schedule by month/year |
| GET | `/api/v1/escalas-evento/governo/{eventoId}` | Event schedule details |
| PATCH | `/api/v1/escalas-evento/governo/{eventoId}` | Update event schedule ministries |

### EscalaMinisterioController
**Base Path**: `/api/v1/escalas-ministerio`

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/v1/escalas-ministerio/lider-ministerio` | Consolidated ministry schedule by leader |
| GET | `/api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}/membros-disponiveis` | Count available ministry members |
| GET | `/api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}` | List ministry members for an event scale |
| GET | `/api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}/{quantidadeMembrosRandomizados}` | Randomize available members |
| POST | `/api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}/revisar-randomizacao/{membroMinisterioIdASerTrocado}` | Review randomized member selection |
| PATCH | `/api/v1/escalas-ministerio/lider-ministerio/{escalaEventoId}` | Save ministry schedule assignments |
| GET | `/api/v1/escalas-ministerio/membro` | Authenticated member schedule |

### DashboardController
**Base Path**: `/api/v1/dashboards`

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/v1/dashboards/membros/kpis` | Member KPIs |
| GET | `/api/v1/dashboards/membros/evolucao` | Member evolution |
| GET | `/api/v1/dashboards/membros/faixa-etaria` | Members by age group |
| GET | `/api/v1/dashboards/membros/genero` | Members by gender |
| GET | `/api/v1/dashboards/ministerios/kpis` | Ministry KPIs |
| GET | `/api/v1/dashboards/ministerios/evolucao/{idMinisterio}` | Ministry evolution |
| GET | `/api/v1/dashboards/ministerios/quantidade-membros` | Member count per ministry |
| GET | `/api/v1/dashboards/ministerios/quantidade-eventos` | Event count per ministry |

### GoogleAuthController
**Base Path**: `/api/v1/auth/google`

OAuth2 Google authorization-code login integration

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/v1/auth/google` | Exchange Google authorization code, validate ID token and return API JWT |

### LoginController
**Base Path**: `/api/v1/auth/login`

Authentication and token generation

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/v1/auth/login` | Authenticate with local email/password and return API JWT |

### CadastroController
**Base Path**: `/api/v1/register`

External registration for non-members

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/v1/register` | List churches available for public registration |
| POST | `/api/v1/register` | Register member through public form |

### PerfilController
**Base Path**: `/api/v1/perfil`

Authenticated member profile retrieval and update

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/v1/perfil` | Get authenticated member profile |
| PATCH | `/api/v1/perfil` | Update authenticated member profile |

---

## 5. DTOs & REQUEST/RESPONSE OBJECTS

### Applications DTOs Layer

#### Login/Auth DTOs (`dtos/login/`)
- Login request/response objects
- Token generation responses

#### Google Auth DTOs (`dtos/googleauth/`)
- `GoogleAuthorizationCodeRequestDTO` - Google authorization-code login request
- `GoogleTokenResponseDTO` - Token response returned by Google token endpoint
- `GoogleIdTokenDTO` - Decoded Google ID token claims
- `GoogleRefreshTokenResponseDTO` - Refresh token response projection
- `GoogleAuthRequestDTO` - Legacy ID token request DTO, not used by `GoogleAuthController`

#### Member DTOs (`dtos/membro/`)
- `MembroCreateDTO` - Create member request
- `MembroUpdateDTO` - Update member request
- `MembroResponseDTO` - Member response
- `MembroDetalheResponseDTO` - Detailed member response
- `MembroSimplificadoDTO` - Simplified member view
- `MembroDashEvolucaoDTO` - Member evolution (dashboard)
- `DashboardFaixaEtariaMembroDTO` - Age group analytics
- `DashboardGeneroMembroDTO` - Gender analytics
- `MembroKpiResponseDTO` - KPI response
- `MembroMinisterioCreateDTO` - Member-ministry link creation
- `MembroMinisterioDTO` - Member-ministry relationship
- `MembroMinisterioInfoMembroDTO` - Member info in ministry context
- `EnderecoMembroDTO` - Member address

#### Event DTOs (`dtos/evento/`)
- `EventoCreateDTO` - Create event request
- `EventoUpdateDTO` - Update event request
- `EventoSimplificadoDTO` - Simple event view
- `EventoCompletoDTO` - Full event details
- `EventoComEventoMinisterioDTO` - Event with ministry mapping
- `EventoUnicoSimplificadoDTO` - Single event simplified
- `EventoKpiDTO` - Event analytics
- `EnderecoEventoDTO` - Event address
- `EnderecoEventoSimplificadoDTO` - Simple address view

#### Ministry DTOs (`dtos/ministerio/`)
- `MinisterioCreateDTO` - Create ministry request
- `MinisterioUpdateDTO` - Update ministry request
- `MinisterioResponseDTO` - Ministry response
- `MinisterioSimplificadoDTO` - Simple ministry view
- `MinisterioSuperSimplificadoDTO` - Minimal ministry view
- `MinisterioDashEvolucaoDTO` - Ministry growth analytics
- `MinisterioDashQuantidadeMembrosDTO` - Members per ministry analytics
- `MinisterioEventoDashDTO` - Event per ministry analytics
- `KpisMembrosDTO` - Member KPIs
- `KpisMinisteriosDTO` - Ministry KPIs

`MinisterioSuperSimplificadoDTO` is used directly in JPQL projections for leader/member ministry lookups.

#### Scale DTOs

**Event Scales** (`dtos/escalasevento/`):
- `EscalaEventoEscaladoDTO` - Event schedule with assignments
- `EscalaEventoConsolidadoDTO` - Consolidated event schedule

**Ministry Scales** (`dtos/escalaministerio/`):
- `EscalaMinisterioDTO` - Ministry member assignment
- `EscalaMinisterioSalvarDTO` - Save ministry assignments request
- `EscalaMinisterioConsolidadoDTO` - Consolidated ministry assignments
- `EscalaMembroMinisterioDTO` - Member's ministry assignments
- `EscalaMembroMinisterioSimplificadoDTO` - Simplified assignments

#### Other DTOs
- `CadastroExternoDTO` - External registration form
- `RestResponseMessageDTO` - Standard API response message
- `RecorrenciaCreateDTO` - Recurrence creation request
- `RecorrenciaSimplificadaDTO` - Simplified recurrence response

### Infrastructure Persistence Projections (`infrastructure/persistence/projection/`)
Projection interfaces for complex database queries (read-only):
- `EscalaEventoEscaladoQueryResult`
- `EscalaEventoQueryResult`
- `EscalaMembroMinisterioQueryResult`
- `EscalaMembroMinisterioSimplificadoQueryResult`
- `EscalaMinisterioConsolidadoQueryResult`
- `EscalaMinisterioQueryResult`

Ministry lookups currently project directly to `MinisterioSuperSimplificadoDTO`.

---

## 6. DOMAIN SERVICES

### EscalaStatusDomainService
**Interface**: `domain/service/EscalaStatusDomainService.java`
**Implementation**: `domain/service/EscalaStatusDomainServiceImpl.java`

**Purpose**: Manages business logic for scale/schedule status transitions and validation

**Key Responsibilities**:
- Recalculate `EscalaEvento` status from `EscalaMinisterio` confirmations
- Recalculate `Evento` status from `EscalaEvento` confirmations
- Propagate pending/confirmed status after schedule assignment changes

---

## 7. MAPPERS

Located in `applications/mappers/`, organized by domain:

- **endereco/** - Address entity mapping
- **evento/** - Event entity mapping
- **igreja/** - Church entity mapping
- **membro/** - Member entity mapping
- **ministerio/** - Ministry entity mapping
- **recorrencia/** - Recurrence entity mapping

Manual Spring mappers that convert between:
- Domain Entities ↔ DTOs
- Request DTOs ↔ Domain Entities

---

## 8. ENUMERATIONS

All enums located in `domain/enums/`:

| Enum | Purpose | Values |
|------|---------|--------|
| `EnumCargoMembro` | Member's global role in church | GOVERNO, LIDER_MINISTERIO, MEMBRO |
| `EnumCargoMembroMinisterio` | Member's role within ministry | LIDER_MINISTERIO, MEMBRO_MINISTERIO |
| `EnumGeneroMembro` | Member's gender | MASCULINO, FEMININO |
| `EnumStatusEscalaMinisterio` | Ministry schedule status | PENDENTE, CONFIRMADO, CONCLUIDO |
| `EnumStatusEvento` | Event status | PENDENTE, CONFIRMADO, CONCLUIDO |
| `EnumStatusMembro` | Member's status in system | ATIVO, INATIVO |
| `EnumStatusMinisterio` | Ministry's operational status | ATIVO, INATIVO |
| `TipoRecorrencia` | Event recurrence pattern | NAO_REPETE, SEMANAL, MENSAL |

---

## 9. CONFIGURATION & UTILITY CLASSES

### Security Configuration (`global/config/SecurityConfig.java`)
- Spring Security configuration
- OAuth2 resource server JWT validation
- Public routes for `/api/v1/auth/**`, `/api/v1/register/**`, OAuth2, H2 and Swagger
- RSA-based JWT encoder/decoder beans
- CORS configuration
- HTTP security rules

### OAuth2 Properties (`global/config/GoogleOAuthProperties.java`)
Configuration properties for Google OAuth integration:
- Client ID
- Client Secret
- Token URI with default `https://oauth2.googleapis.com/token`

### JWT Utilities (`global/util/JwtUtils.java`)
- Claims extraction
- Authenticated member subject extraction
- Authenticated church id extraction
- Authenticated role extraction

### ID Utilities (`global/util/IdEntityUtils.java`)
- UUID generation/validation
- Entity ID handling

### RabbitMQ Configuration (`global/config/RabbitMQConfig.java`)
- Topic exchange, queue and binding for created event messages

### Sensitive Field Utilities
- AES/GCM encryption for sensitive fields
- Deterministic hashes for exact lookup of encrypted fields

---

## 10. ERROR HANDLING

### Custom Exceptions
Located in `global/error/exceptions/`:

- `BadCredentialsException` - Authentication/authorization failures (HTTP 401)
- `FieldInvalidException` - Invalid business input (HTTP 400)
- `GoogleAuthorizationCodeException` - Google authorization-code validation failure (HTTP 401)
- `GoogleOAuthIntegrationException` - Google OAuth communication/configuration failure (HTTP 502)
- `ObjectExistsException` - Conflict with existing object (HTTP 409)
- `ObjectNotFoundException` - Object not found (HTTP 404)
- `ObjectSaveErrorException` - Object save/validation failure (HTTP 400)

Located in `infrastructure/exceptions/`:

- `DateInvalidException` - Invalid date format/value (HTTP 400)
- `TimeInvalidException` - Invalid time format/value (HTTP 400)
- `MembroNaoEncontradoException` - Legacy member not found exception
- `MinisterioNaoEncontradoException` - Legacy ministry not found exception

### Global Error Handling
- Located in `global/error/`
- Custom exception handlers
- Standardized error response format
- `ApiErrorsComuns` annotation for Swagger documentation

---

## 11. DATA ACCESS & PERSISTENCE

### JPA Repository Pattern
Domain repositories are implemented by infrastructure gateways backed by Spring Data JPA repositories:
- Pagination support via `Pageable`
- Custom query methods
- Filter and search functionality

### Query Implementations
Complex queries return specialized result objects:
- Query result DTOs for optimization
- Avoiding N+1 query problems
- Efficient filtering and aggregation

### Entities with Relationships
```
Membro (Member) ──┬── MembroMinisterio ──── Ministerio
                  ├── EscalaMinisterio ─┬── EscalaEvento ──── Evento
                  └── EnderecoMembro     └────────────────── Igreja
Igreja ───────────┬── Ministerio
                  ├── Evento
                  └── Membro
```

---

## 12. KEY FEATURES BY DOMAIN

### Members Management
✓ Create member with address  
✓ Search with pagination and filters  
✓ Update member profile  
✓ View member history  
✓ Track member by email  

### Ministries Management
✓ Create/edit ministry  
✓ Manage ministry members (add/remove)  
✓ Role-based access (Governo, Líder, Membro)  
✓ Ministry search and filtering  
✓ Assign member roles within ministry  

### Event Management
✓ Create recurring events  
✓ Update/delete events  
✓ Search events by month/year  
✓ Manage event locations  
✓ Multiple recurrence types  

### Scheduling & Escalas
✓ Generate event schedules  
✓ Assign ministry members to events  
✓ Auto-randomization of assignments  
✓ Review and confirm assignments  
✓ View consolidated schedules  

### Analytics & Dashboard
✓ Member growth trends  
✓ Members by age group  
✓ Members by gender  
✓ Ministry statistics  
✓ Events per ministry  
✓ Members per ministry  

### Authentication & Authorization
✓ Google OAuth2 authorization-code integration
✓ JWT token-based API security  
✓ Role-based access control  
✓ Local email/password authentication

---

## 13. EXTERNAL INTEGRATIONS

### Google OAuth2
- Authorization code exchange: `GoogleAuthorizationCodeExchangerImpl`
- ID token verification: `GoogleIdTokenVerifierImpl`
- Client configuration: `GoogleOAuthProperties`
- Refresh token persistence: `GoogleRefreshTokenMembroRepository`
- OAuth2 client handlers still exist: `CustomOAuth2AuthenticationSuccessHandler`, `CustomOidcUserService`

### RabbitMQ
- Event publication: `EventoProducer`
- Exchange/queue/binding configuration: `RabbitMQConfig`
- Created event payload: `EventoCriadoMessageDTO`

### Spring Security
- OAuth2 resource server and OAuth2 client dependencies
- JWT token validation
- CORS support
- Role-based access control

---

## 14. API SPECIFICATION

### Request/Response Format
- **Format**: JSON
- **Standard Response**: `RestResponseMessageDTO`
- **Pagination**: Spring `Page<T>`
- **Error Handling**: Custom exception handlers with standardized error response

### Authentication
- **Type**: JWT resource server + local login + Google authorization-code login
- **Header**: `Authorization: Bearer {token}`
- **Token Generation**: Via `LoginServiceUseCase`, `LoginGoogleUseCase` and `GenerateTokenUseCase`

### Validation
- Input validation on DTOs
- Business logic validation in UseCases
- Database constraint validation

---

## 15. PROJECT STATISTICS

| Category | Count |
|----------|-------|
| Controllers | 10 |
| Domain Entities | 12 |
| Enumerations | 8 |
| Repository Interfaces | 10 |
| UseCase Classes | 59 |
| DTO Classes | 56 |
| Mappers | 9 |
| Custom Exceptions | 11 |
| Domain Services | 2 files / 1 service |

---

## 16. TECHNOLOGY STACK

- **Runtime**: Java 21
- **Framework**: Spring Boot 3.x
- **Database**: JPA/Hibernate with H2 default and environment-configurable datasource
- **Authentication**: Spring Security + OAuth2 client/resource server + JWT
- **API Documentation**: Springdoc OpenAPI 3.0
- **Build**: Maven
- **Messaging**: Spring AMQP / RabbitMQ
- **Mapping**: Manual Spring mappers
- **Validation**: Jakarta Bean Validation (formerly javax.validation)

---

## 17. NOTES FOR DEVELOPERS

1. **UseCase Layer**: Business logic is wrapped in UseCase classes. Always create a dedicated UseCase for each operation.

2. **DTOs**: Multiple DTOs exist for different contexts (Create, Update, Response, Simplified, Dashboard). Choose the appropriate one.

3. **Filters**: Many List operations support filters via query parameters (buscaGeral, status, etc.)

4. **Recurrence**: Events support multiple recurrence types managed via `Recorrencia` entity.

5. **Roles**: Three main roles exist:
   - **GOVERNO**: Administrator with full system access
   - **LIDER_MINISTERIO**: Ministry leader managing their team
   - **MEMBRO**: Regular member with limited access

6. **Escalas**: Event schedules (EscalaEvento) are generated and then ministry members are assigned (EscalaMinisterio).

7. **OAuth2**: Google API login receives an authorization code, exchanges it with Google, validates the returned ID token, optionally stores the refresh token, and emits the backend JWT.

8. **Error Handling**: Always use custom exceptions for business logic errors; they're automatically mapped to HTTP responses.

---

**Analysis Generated**: May 2026
**Project Type**: Spring Boot Clean Architecture  
**Version**: Based on current codebase structure
