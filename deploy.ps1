$RG_NAME = "rg-dimdim-carlos"
$LOCATION = "canadacentral"
$SQL_SERVER_NAME = "sqlserver-dimdim-566022"
$SQL_DB_NAME = "nexus_verde_db"
$SQL_ADMIN = "carlosadmin"
$SQL_PASSWORD = "Fiap#2026"
$APP_PLAN_NAME = "plan-dimdim-566022"
$WEBAPP_NAME = "webapp-dimdim-566022"

az group create --name $RG_NAME --location $LOCATION
az sql server create --name $SQL_SERVER_NAME --resource-group $RG_NAME --location $LOCATION --admin-user $SQL_ADMIN --admin-password $SQL_PASSWORD
az sql server firewall-rule create --resource-group $RG_NAME --server $SQL_SERVER_NAME --name AllowAzureServices --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0
az sql server firewall-rule create --resource-group $RG_NAME --server $SQL_SERVER_NAME --name AllowAll --start-ip-address 0.0.0.0 --end-ip-address 255.255.255.255
az sql db create --resource-group $RG_NAME --server $SQL_SERVER_NAME --name $SQL_DB_NAME --service-objective Basic

az extension add -n application-insights
az monitor app-insights component create --app "appinsights-$WEBAPP_NAME" --location $LOCATION --kind web -g $RG_NAME --application-type web
$APP_INSIGHTS_KEY = az monitor app-insights component show --app "appinsights-$WEBAPP_NAME" -g $RG_NAME --query instrumentationKey -o tsv

az appservice plan create --name $APP_PLAN_NAME --resource-group $RG_NAME --sku B1 --is-linux
az webapp create --resource-group $RG_NAME --plan $APP_PLAN_NAME --name $WEBAPP_NAME --runtime "JAVA|17-java17"

az webapp config appsettings set --resource-group $RG_NAME --name $WEBAPP_NAME --settings SPRING_DATASOURCE_URL="jdbc:sqlserver://$($SQL_SERVER_NAME).database.windows.net:1433;database=$SQL_DB_NAME;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;" SPRING_DATASOURCE_USERNAME=$SQL_ADMIN SPRING_DATASOURCE_PASSWORD=$SQL_PASSWORD APPINSIGHTS_INSTRUMENTATIONKEY=$APP_INSIGHTS_KEY APPLICATIONINSIGHTS_CONNECTION_STRING="InstrumentationKey=$APP_INSIGHTS_KEY"

az webapp deploy --resource-group $RG_NAME --name $WEBAPP_NAME --src-path target/nexus-verde-3.2.5.jar --type jar