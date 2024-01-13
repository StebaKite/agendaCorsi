package com.example.agendaCorsi.ui.base;

import android.util.Log;

import com.example.agendaCorsi.AgendaCorsiApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class QueryComposer {

    private static QueryComposer INSTANCE = null;
    public static PropertyReader propertyReader;
    public static Properties properties;

    private QueryComposer() {}

    /**
     * Singleton Pattern
     * @return class instance
     */

    public static QueryComposer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new QueryComposer();
            propertyReader = new PropertyReader(AgendaCorsiApp.getContext());
            properties = propertyReader.getMyProperties("config.properties");
        }
        return INSTANCE;
    }

    public String getQuery(String queryName) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(AgendaCorsiApp.getContext().getAssets().open(properties.getProperty("QUERY_APPLICATION_PATH") + "/" + queryName + ".sql")));
            String line = br.readLine();
            while (line != null) {

                // Salto le linee vuote e le linee di commento
                if (line.trim().startsWith("--") || line.trim().startsWith("  ")) {}
                else {sb.append(line.trim()).append(" ");}

                line = br.readLine();
            }
            br.close();
        }
        catch (IOException e) {
            Log.e("QueryComposer", e.getMessage());
        }
        return sb.toString();
    }
}
