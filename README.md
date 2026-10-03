# Projeto Checkpoint (CP) - Nexus Verde: Web App em Java com Azure PaaS

Este repositório contém a aplicação desenvolvida em **Java (Spring Boot)** para o checkpoint de DevOps, utilizando uma arquitetura em nuvem na Microsoft Azure com o modelo Master-Detail (**Pessoa / Profissão**).

---

## 🛠️ Tecnologias Utilizadas
* **Linguagem:** Java 17
* **Framework:** Spring Boot 3.2.5 (Spring Data JPA, Web, Validation)
* **Base de Dados:** Azure SQL Server (PaaS)
* **Hospedagem:** Azure App Service (Linux Web App)
* **Monitoramento:** Azure Application Insights
* **Automação/Deploy:** Azure CLI (`az cli`)

---

## 🗂️ Estrutura do Projeto (Master-Detail)
O projeto implementa uma relação de 1 para N entre **Profissão** e **Pessoa**:
* **Profissão:** Entidade Master (ID, Nome, Descrição)
* **Pessoa:** Entidade Detail (ID, Nome, Idade, ID_Profissao com chave estrangeira)

---

## 📋 Pré-requisitos
* Java JDK 17 instalado
* Maven instalado (ou IntelliJ IDEA com Maven integrado)
* Azure CLI instalada e autenticada (`az login`)

---

## 🚀 Guia de Implantação (How-To / Deploy)

Para subir toda a infraestrutura e a aplicação para o Azure, siga os passos abaixo no seu terminal PowerShell:

### 1. Gerar o empacotamento JAR
No seu computador, abra o projeto e gere o build da aplicação:
```bash
mvn clean package -DskipTests


(Isto irá gerar o ficheiro nexus-verde-3.2.5.jar dentro da pasta target).

2. Executar o Script de Infraestrutura e Deploy (Azure CLI)
Certifique-se de que está logado no Azure (az login) e execute o seguinte script no PowerShell para criar os recursos (Grupo de Recursos, Servidor SQL, Base de Dados, App Service Plan, Web App, Application Insights e Injeção de Variáveis):


$RG_NAME = "rg-dimdim-carlos"
$LOCATION = "canadacentral"
$SQL_SERVER_NAME = "sqlserver-dimdim-566022"
$SQL_DB_NAME = "nexus_verde_db"
$SQL_ADMIN = "carlosadmin"
$SQL_PASSWORD = "Fiap#2026"
$APP_PLAN_NAME = "plan-dimdim-566022"
$WEBAPP_NAME = "webapp-dimdim-566022"

# Criar Grupo de Recursos e Banco de Dados PaaS
az group create --name $RG_NAME --location $LOCATION
az sql server create --name $SQL_SERVER_NAME --resource-group $RG_NAME --location $LOCATION --admin-user $SQL_ADMIN --admin-password $SQL_PASSWORD
az sql server firewall-rule create --resource-group $RG_NAME --server $SQL_SERVER_NAME --name AllowAzureServices --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0
az sql server firewall-rule create --resource-group $RG_NAME --server $SQL_SERVER_NAME --name AllowAll --start-ip-address 0.0.0.0 --end-ip-address 255.255.255.255
az sql db create --resource-group $RG_NAME --server $SQL_SERVER_NAME --name $SQL_DB_NAME --service-objective Basic

# Configurar Application Insights
az extension add -n application-insights
az monitor app-insights component create --app "appinsights-$WEBAPP_NAME" --location $LOCATION --kind web -g $RG_NAME --application-type web
$APP_INSIGHTS_KEY = az monitor app-insights component show --app "appinsights-$WEBAPP_NAME" -g $RG_NAME --query instrumentationKey -o tsv

# Criar Web App (Linux + Java 17)
az appservice plan create --name $APP_PLAN_NAME --resource-group $RG_NAME --sku B1 --is-linux
az webapp create --resource-group $RG_NAME --plan $APP_PLAN_NAME --name $WEBAPP_NAME --runtime "JAVA|17-java17"

# Injetar Variaveis de Ambiente e Ligação ao Banco
az webapp config appsettings set --resource-group $RG_NAME --name $WEBAPP_NAME --settings SPRING_DATASOURCE_URL="jdbc:sqlserver://$($SQL_SERVER_NAME).database.windows.net:1433;database=$SQL_DB_NAME;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;" SPRING_DATASOURCE_USERNAME=$SQL_ADMIN SPRING_DATASOURCE_PASSWORD=$SQL_PASSWORD APPINSIGHTS_INSTRUMENTATIONKEY=$APP_INSIGHTS_KEY APPLICATIONINSIGHTS_CONNECTION_STRING="InstrumentationKey=$APP_INSIGHTS_KEY"

# Realizar o Deploy do JAR
az webapp deploy --resource-group $RG_NAME --name $WEBAPP_NAME --src-path target/nexus-verde-3.2.5.jar --type jar

💾 Inicialização da Base de Dados (DML / DDL)
O script completo de criação das tabelas e carga inicial encontra-se no ficheiro init.sql na raiz deste repositório.
Para aplicá-lo:

Acesse o Portal do Azure.

Vá até ao seu banco de dados (nexus_verde_db).

Abra o Editor de consultas (Query editor), faça login com carlosadmin e Fiap#2026.

Cole e execute o conteúdo do ficheiro init.sql.

👨‍💻 Aluno
Nome: Carlos Alberto Guedes Neto, Eduardo Novaes, Mathaus Victor, Luan Peixoto, Vinícius L. E. M. Garcia

RM: 566022, 561515, 564146, 562258, 563340

