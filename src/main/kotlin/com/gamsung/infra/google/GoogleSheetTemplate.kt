package com.gamsung.infra.google

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.ServiceAccountCredentials
import org.springframework.core.io.ClassPathResource

class GoogleSheetTemplate(
    private val serviceAccountClientEmail: String,
    private val appName: String,
) {
    fun getSheet(sheetId: String, sheet: String): ValueRange? {
        val service = Sheets.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
            HttpCredentialsAdapter(
                ServiceAccountCredentials.fromStream(ClassPathResource("gamsung-routine-c5cbd1857270.json").inputStream)
                    .createScoped(listOf(SheetsScopes.SPREADSHEETS))
                    .createDelegated(serviceAccountClientEmail)
            )
        ).setApplicationName(appName).build()

        return service.spreadsheets().values().get(sheetId, sheet).execute()
    }
}