# Diacono Backend Project - Comprehensive Analysis

## Executive Summary
The Diacono Backend is a Spring Boot application built with **Clean Architecture** principles using the **UseCase pattern**. It's designed to manage church membership, ministries, events, and scheduling systems.

---

## 1. ARCHITECTURE OVERVIEW

### Design Pattern
- **Primary Pattern**: Clean Architecture with UseCase (Commands) pattern
- **Framework**: Spring Boot with Spring Data JPA
- **Build Tool**: Maven
- **Authentication**: OAuth2 (Google, Government)
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
- **Main Class**: `DiaconoApplication.java` - Spring Boot entry point with CommandLineRunner for data initialization

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
Spring Data JPA repository interfaces:
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
- **MembroController** - Member operations (POST create, GET list with/without filters)
- **MinisteriosController** - Ministry operations (create, edit, list, member management)
- **EventoController** - Event operations (create, update, delete, search by month/year)
- **EscalaEventoController** - Event schedule management
- **EscalaMinisterioController** - Ministry schedule management
- **DashboardController** - Analytics and KPIs
- **GoogleAuthController** - Google OAuth integration
- **LoginController** - Login/authentication endpoints
- **CadastroController** - External registration/signup

#### Persistence (`infrastructure/persistence/`)
JPA implementations and query results:

**Membro Persistence**:
- `MembroJpaRepository` - Spring Data JPA interface
- `MembroRepositoryImpl` - Custom implementation

**Ministry-member lookup**:
- `MinisteriosMembros/` - consultas de ministérios do membro autenticado

**Query Results** (specialized DTOs for complex queries):
- `EscalaEventoEscaladoQueryResult`
- `EscalaEventoQueryResult`
- `EscalaMembroMinisterioQueryResult`
- `EscalaMembroMinisterioSimplificadoQueryResult`
- `EscalaMinisterioConsolidadoQueryResult`
- `EscalaMinisterioQueryResult`

Observação: a consulta de ministérios do membro usa projeção direta para `MinisterioSuperSimplificadoDTO`; não existe `MinisterioSuperSimplificadoQueryResult` no código atual.

**Other Persistence Folders**:
- `EnderecoEvento/`, `EscalaEvento/`, `EscalaMinisterio/` - Specific entity implementations
- `Evento/`, `Igreja/`, `Ministerios/` - Ministry-related persistence
- `MembroMinisterio/`, `Recorrencia/` - Relationship and configuration entities
- `Googleauth/` - OAuth token persistence

#### Exceptions (`infrastructure/exceptions/`)
Custom exception classes:
- `DateInvalidException` - Invalid date input
- `TimeInvalidException` - Invalid time input
- `MembroNaoEncontradoException` - Member not found
- `MinisterioNaoEncontradoException` - Ministry not found

#### Auth Integration (`infrastructure/auth/`)
Google OAuth2 integration:
- `GoogleIdTokenVerifier` - Interface for token verification
- `GoogleIdTokenVerifierImpl` - Google token validation implementation

### 3.4 UseCase Package (`usecases/`)
Application-specific business logic organized by domain

#### Root UseCase Classes
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
- `BuscarMinisterioPorUUIDUseCase` - Get ministry by ID

#### Member UseCase (`usecases/membro/`)
Member operations:
- `CriarMembroUseCase` - Create new member
- `CadastrarMembroUseCase` - Register via external form
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
- `BuscarKpiQuantidadeMembrosPerMinisterioDashUseCase` - Members per ministry
- `buscarKpiQuantidadeEventosPorMinisterioDashUseCase` - Events per ministry

#### Church UseCase (`usecases/igreja/`)
Church information:
- `BuscasIgrejasUseCase` - List churches
- `BuscarIgrejaPorUUIDUseCase` - Get church by ID

#### Google Auth UseCase (`usecases/googleauth/`)
OAuth2 integrations:
- `LoginGoogleUseCase` - Google login
- `AutenticarGoogleUseCase` - Google authentication
- `AtualizarSecretGoogleUseCase` - Update Google OAuth secrets

### 3.5 Global Package (`global/`)
Cross-cutting concerns

#### Configuration (`global/config/`)
- `GoogleOAuthProperties` - Google OAuth configuration properties
- `SecurityConfig` - Spring Security configuration (OAuth2, JWT)

#### Error Handling (`global/error/`)
- **exceptions/** - Custom exception hierarchy
- **comuns/** - Common error response handling
- **error/** - Error response DTOs

#### Utilities (`global/util/`)
- `JwtUtils` - JWT token generation/validation
- `IdEntityUtils` - UUID/ID utilities

---

## 4. CONTROLLERS & ENDPOINTS

### MembroController
**Base Path**: `/membros`

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/membros` | Create new member |
| GET | `/membros` | List members (with optional filters: search, status, ministry) |

### MinisteriosController
**Base Path**: `/api/v1/ministerios`

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/v1/ministerios` | Create ministry |
| PUT | `/api/v1/ministerios/{id}` | Update ministry |
| GET | `/api/v1/ministerios` | Get all active ministries |
| GET | `/api/v1/ministerios/governo` | Admin view of ministries (with filters) |
| GET | `/api/v1/ministerios/lider` | Ministry leader's view |
| GET | `/api/v1/ministerios/membro` | Authenticated member's ministries |
| GET | `/api/v1/ministerios/{id}/membros` | Members of specific ministry |
| POST | `/api/v1/ministerios/{id}/membros` | Add member to ministry |
| DELETE | `/api/v1/ministerios/{id}/membros/{membroId}` | Remove member from ministry |

### EventoController
**Base Path**: `/api/v1/eventos`

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/v1/eventos` | Get events by month/year |
| GET | `/api/v1/eventos/{id}` | Get specific event details |
| GET | `/api/v1/eventos/enderecos` | Get all event addresses |
| POST | `/api/v1/eventos` | Create event |
| PUT | `/api/v1/eventos/{id}` | Update event |
| DELETE | `/api/v1/eventos/unico/{id}` | Delete single event |
| DELETE | `/api/v1/eventos/multiplos` | Delete multiple events |

### EscalaEventoController
**Base Path**: `/api/v1/escalas-evento`

Manages event schedule generation and viewing

### EscalaMinisterioController
**Base Path**: `/api/v1/escalas-ministerio`

Manages ministry member assignments to events

### DashboardController
**Base Path**: `/api/v1/dashboard`

KPI and analytics endpoints for members and ministries

### GoogleAuthController
**Base Path**: `/api/v1/google-auth`

OAuth2 Google login integration

### LoginController
**Base Path**: `/api/v1/login`

Authentication and token generation

### CadastroController
**Base Path**: `/api/v1/cadastro`

External registration for non-members

---

## 5. DTOs & REQUEST/RESPONSE OBJECTS

### Applications DTOs Layer

#### Login/Auth DTOs (`dtos/login/`)
- Login request/response objects
- Token generation responses

#### Member DTOs (`dtos/membro/`)
- `MembroCreateDTO` - Create member request
- `MembroUpdateDTO` - Update member request
- `MembroResponseDTO` - Member response
- `MembroSimplificadoDTO` - Simplified member view
- `MembroDashEvolucaoDTO` - Member evolution (dashboard)
- `MembroDashFaixaEtariaDTO` - Age group analytics
- `MembroDashGeneroDTO` - Gender analytics
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
- `MinisterioKpisResponseDTO` - Ministry KPIs
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
- `RecorrenciaDTO` - Recurrence configuration

### Infrastructure Persistence DTOs (`infrastructure/persistence/dtos/`)
Query result objects for complex database queries (read-only):
- `EscalaEventoEscaladoQueryResult`
- `EscalaEventoQueryResult`
- `EscalaMembroMinisterioQueryResult`
- `EscalaMembroMinisterioSimplificadoQueryResult`
- `EscalaMinisterioConsolidadoQueryResult`
- `EscalaMinisterioQueryResult`

Ministry lookups currently project directly to `MinisterioSuperSimplificadoDTO`; there is no dedicated `MinisterioSuperSimplificadoQueryResult`.

---

## 6. DOMAIN SERVICES

### EscalaStatusDomainService
**Interface**: `domain/service/EscalaStatusDomainService.java`
**Implementation**: `domain/service/EscalaStatusDomainServiceImpl.java`

**Purpose**: Manages business logic for scale/schedule status transitions and validation

**Key Responsibilities**:
- Status validation
- Status transitions
- Business rule enforcement for scheduling

---

## 7. MAPPERS

Located in `applications/mappers/`, organized by domain:

- **endereco/** - Address entity mapping
- **evento/** - Event entity mapping
- **igreja/** - Church entity mapping
- **membro/** - Member entity mapping
- **ministerio/** - Ministry entity mapping
- **recorrencia/** - Recurrence entity mapping

MapStruct or manual mappers that convert between:
- Domain Entities ↔ DTOs
- DTOs ↔ Persistence Models

---

## 8. ENUMERATIONS

All enums located in `domain/enums/`:

| Enum | Purpose | Values |
|------|---------|--------|
| `EnumCargoMembro` | Member's global role in church | (Deacon, Member, etc.) |
| `EnumCargoMembroMinisterio` | Member's role within ministry | (Leader, Coordinator, Volunteer, etc.) |
| `EnumGeneroMembro` | Member's gender | M, F, Other |
| `EnumStatusEscalaMinisterio` | Ministry schedule status | (Confirmed, Pending, Cancelled, etc.) |
| `EnumStatusEvento` | Event status | (Scheduled, Cancelled, Completed, etc.) |
| `EnumStatusMembro` | Member's status in system | (Active, Inactive, Blocked, etc.) |
| `EnumStatusMinisterio` | Ministry's operational status | (Active, Inactive, etc.) |
| `TipoRecorrencia` | Event recurrence pattern | (Weekly, Biweekly, Monthly, etc.) |

---

## 9. CONFIGURATION & UTILITY CLASSES

### Security Configuration (`global/config/SecurityConfig.java`)
- Spring Security configuration
- OAuth2 authentication provider setup
- JWT token validation
- CORS configuration
- HTTP security rules

### OAuth2 Properties (`global/config/GoogleOAuthProperties.java`)
Configuration properties for Google OAuth integration:
- Client ID
- Client Secret
- Redirect URIs
- Scopes

### JWT Utilities (`global/util/JwtUtils.java`)
- Token generation
- Token validation
- Claims extraction
- Token expiration handling

### ID Utilities (`global/util/IdEntityUtils.java`)
- UUID generation/validation
- Entity ID handling

---

## 10. ERROR HANDLING

### Custom Exceptions
Located in `infrastructure/exceptions/`:

- `MembroNaoEncontradoException` - When member doesn't exist (HTTP 404)
- `MinisterioNaoEncontradoException` - When ministry doesn't exist (HTTP 404)
- `DateInvalidException` - Invalid date format/value (HTTP 400)
- `TimeInvalidException` - Invalid time format/value (HTTP 400)

### Global Error Handling
- Located in `global/error/`
- Custom exception handlers
- Standardized error response format
- ApiErrorCommons annotation for Swagger documentation

---

## 11. DATA ACCESS & PERSISTENCE

### JPA Repository Pattern
All repositories extend `JpaRepository` with custom queries:
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
✓ Google OAuth2 integration  
✓ Government authentication  
✓ JWT token-based API security  
✓ Role-based access control  

---

## 13. EXTERNAL INTEGRATIONS

### Google OAuth2
- Authentication handler: `CustomOAuth2AuthenticationSuccessHandler`
- OIDC User service: `CustomOidcUserService`
- Token verification: `GoogleIdTokenVerifierImpl`
- Token refresh: `GoogleRefreshTokenMembroRepository`

### Spring Security
- OAuth2 authentication
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
- **Type**: OAuth2 + JWT
- **Header**: `Authorization: Bearer {token}`
- **Token Generation**: Via `LoginServiceUseCase` and `GenerateTokenUseCase`

### Validation
- Input validation on DTOs
- Business logic validation in UseCases
- Database constraint validation

---

## 15. PROJECT STATISTICS

| Category | Count |
|----------|-------|
| Controllers | 9 |
| Domain Entities | 12 |
| Enumerations | 8 |
| Repository Interfaces | 10 |
| UseCase Classes | 40+ |
| DTO Classes | 50+ |
| Mappers | 6 |
| Custom Exceptions | 4 |
| Domain Services | 1 (core) |

---

## 16. TECHNOLOGY STACK

- **Runtime**: Java 11+
- **Framework**: Spring Boot 3.x
- **Database**: JPA/Hibernate (MySQL/PostgreSQL)
- **Authentication**: Spring Security + OAuth2 + JWT
- **API Documentation**: Springdoc OpenAPI 3.0
- **Build**: Maven
- **Mapping**: (MapStruct or manual)
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

7. **OAuth2**: Google authentication is handled via custom handlers that create/update Membro records.

8. **Error Handling**: Always use custom exceptions for business logic errors; they're automatically mapped to HTTP responses.

---

**Analysis Generated**: April 2026  
**Project Type**: Spring Boot Clean Architecture  
**Version**: Based on current codebase structure
