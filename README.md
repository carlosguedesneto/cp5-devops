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
Certifique-se de que está logado no Azure (az login) e execute o seguinte script no PowerShell para criar os recursos:

PowerShell
$RG = "rg-dimdim-carlos"
$LOC = "canadacentral"
$SQL = "sqlserver-dimdim-566022"
$DB = "nexus_verde_db"
$USER = "carlosadmin"
$PASS = "Fiap#2026"
$PLAN = "plan-dimdim-566022"
$APP = "webapp-dimdim-566022"

az group create --name $RG --location $LOC
az sql server create --name $SQL -g $RG -l $LOC --admin-user $USER --admin-password $PASS
az sql server firewall-rule create -g $RG --server $SQL --name AllowAll --start-ip-address 0.0.0.0 --end-ip-address 255.255.255.255
az sql db create -g $RG --server $SQL --name $DB --service-objective Basic

az extension add -n application-insights
az monitor app-insights component create --app "appinsights-$APP" -l $LOC --kind web -g $RG --application-type web
$KEY = az monitor app-insights component show --app "appinsights-$APP" -g $RG --query instrumentationKey -o tsv

az appservice plan create --name $PLAN -g $RG --sku B1 --is-linux
az webapp create -g $RG --plan $PLAN --name $APP --runtime "JAVA|17-java17"

az webapp config appsettings set -g $RG --name $APP --settings SPRING_DATASOURCE_URL="jdbc:sqlserver://$($SQL).database.windows.net:1433;database=$DB;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;" SPRING_DATASOURCE_USERNAME=$USER SPRING_DATASOURCE_PASSWORD=$PASS APPINSIGHTS_INSTRUMENTATIONKEY=$KEY APPLICATIONINSIGHTS_CONNECTION_STRING="InstrumentationKey=$KEY"

az webapp deploy -g $RG --name $APP --src-path target/nexus-verde-3.2.5.jar --type jar
💾 Inicialização da Base de Dados (DML / DDL)
O script completo de criação das tabelas e carga inicial encontra-se no ficheiro init.sql na raiz deste repositório.
Para aplicá-lo:

Acesse o Portal do Azure.

Vá até ao seu banco de dados (nexus_verde_db).

Abra o Editor de consultas (Query editor), faça login com carlosadmin e Fiap#2026.

Cole e execute o conteúdo do ficheiro init.sql.

👨‍💻 Alunos
Integrantes: Carlos Alberto Guedes Neto, Eduardo Novaes, Mathaus Victor, Luan Peixoto, Vinícius L. E. M. Garcia

RMs: 566022, 561515, 564146, 562258, 563340

Instituição: FIAP
