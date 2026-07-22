# Sistema de Votação - Cooperativa

Sistema de votação para cooperativas desenvolvido com Java 21 e Spring Boot 3.

## 🚀 Tecnologias

- Java 21
- Spring Boot 3.2.1
- Spring Data JPA
- H2 Database (desenvolvimento)
- PostgreSQL (produção)
- Lombok
- SpringDoc OpenAPI (Swagger)
- Spring Cache
- Spring Boot Actuator

## 📋 Funcionalidades

### Funcionalidades Principais
- ✅ Cadastrar nova pauta
- ✅ Abrir sessão de votação (tempo customizável ou 1 minuto por default)
- ✅ Receber votos dos associados (Sim/Não)
- ✅ Contabilizar votos e calcular resultado
- ✅ Validação de CPF (um voto por associado por pauta)

### Tarefas Bônus Implementadas
- ✅ **Bônus 1**: Facade/Client Fake para validação de CPF
  - Valida se CPF é válido
  - Retorna ABLE_TO_VOTE ou UNABLE_TO_VOTE aleatoriamente
  - Retorna 404 se CPF inválido
  
- ✅ **Bônus 2**: Performance
  - Cache de resultados com Spring Cache
  - Queries otimizadas no banco
  - Prepared statements via JPA
  
- ✅ **Bônus 3**: Versionamento de API
  - API versionada em `/api/v1/`
  - Preparada para evolução futura

## 🔧 Instalação e Execução

### Pré-requisitos
- Java 21 ou superior
- Maven 3.6+

### Executando a aplicação

```bash
# Clonar o repositório
git clone <url-do-repositorio>
cd votacao-backend

# Compilar e executar
mvn clean install
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080/api/v1`

### Acessando o Swagger UI
```
http://localhost:8080/api/v1/swagger-ui.html
```

### Acessando o H2 Console
```
http://localhost:8080/api/v1/h2-console
```
- JDBC URL: `jdbc:h2:file:./data/votacao`
- User: `sa`
- Password: (deixe em branco)

## 📡 Endpoints da API

### Pautas

#### Criar Pauta
```http
POST /api/v1/pautas
Content-Type: application/json

{
  "titulo": "Aprovação do Orçamento 2024",
  "descricao": "Votação para aprovação do orçamento anual"
}
```

#### Listar Pautas
```http
GET /api/v1/pautas
```

#### Buscar Pauta
```http
GET /api/v1/pautas/{id}
```

### Sessões de Votação

#### Abrir Sessão
```http
POST /api/v1/pautas/{pautaId}/sessoes
Content-Type: application/json

{
  "duracaoSegundos": 60
}
```

#### Buscar Sessão
```http
GET /api/v1/pautas/{pautaId}/sessoes
```

### Votos

#### Registrar Voto
```http
POST /api/v1/pautas/{pautaId}/votos
Content-Type: application/json

{
  "cpfAssociado": "12345678901",
  "voto": "SIM"
}
```

#### Calcular Resultado
```http
GET /api/v1/pautas/{pautaId}/votos/resultado
```

Resposta:
```json
{
  "pautaId": 1,
  "tituloPauta": "Aprovação do Orçamento 2024",
  "totalVotos": 15,
  "votosSim": 10,
  "votosNao": 5,
  "resultado": "APROVADA"
}
```

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

```
Controller → Service → Repository → Database
     ↓           ↓
   DTOs      Entities
```

### Camadas

- **Controller**: Recebe requisições HTTP e retorna respostas
- **Service**: Contém a lógica de negócio
- **Repository**: Acesso a dados (JPA)
- **Entity**: Modelos de domínio
- **DTO**: Objetos de transferência de dados
- **Client**: Integração com serviços externos (validação de CPF)
- **Exception**: Tratamento centralizado de erros

## 🧪 Testes

### Executando testes
```bash
mvn test
```

### Cobertura de Testes
- Testes unitários de services
- Testes de controllers
- Testes de validação
- Testes de exceções

## 📊 Monitoramento

### Actuator Endpoints
```
GET /api/v1/actuator/health
GET /api/v1/actuator/info
GET /api/v1/actuator/metrics
```

## 🔒 Segurança

Para fins de exercício, a segurança foi abstraída conforme solicitado. Em produção, recomenda-se:
- Implementar autenticação JWT
- Usar HTTPS
- Implementar rate limiting
- Validar permissões por perfil de usuário

## 📝 Logs

Logs são gravados em:
- Console (nível DEBUG para desenvolvimento)
- Arquivo: `logs/votacao.log`

## 🔄 Migração para Produção

Para usar PostgreSQL em produção:

1. Adicione a dependência do PostgreSQL no `pom.xml` (já incluída)
2. Configure o `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/votacao
    username: postgres
    password: sua-senha
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

## 📦 Build para Produção

```bash
mvn clean package -DskipTests
java -jar target/votacao-1.0.0.jar
```
