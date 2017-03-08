public String composeJSONfromSQLite() {
     ArrayList<HashMap<String, String>> offlineList;
     offlineList = new ArrayList<HashMap<String, String>>();
     String selectQuery = "SELECT  * FROM SQLiteDatabase "; // Need to change database name.
     SQLiteDatabase database = this.getWritableDatabase();
     Cursor cursor = database.rawQuery(selectQuery, null);
     if (cursor.moveToFirst()) {
     do {
     HashMap<String, String> map = new HashMap<String, String>();
     map.put("id", cursor.getString(1));
     map.put("password", cursor.getString(2));
     offlineList.add(map);

      } while (cursor.moveToNext());
      }
     database.close();
     Gson gson = new GsonBuilder().create();
     //Use GSON to serialize Array List to JSON
     return gson.toJson(offlineList);
}

public void syncSQLiteMySQLDB() {

   //i get my json string from sqlite, see the code i posted above about this
        final String json = loadCheckoutDB.composeJSONfromSQLite();

        new Thread() {
            public void run() {
                makeRequest("http://myexample/offline/api", json);
            }
        }.start();

    }

    public void makeRequest(String uri, String json) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("session_id", getapikey());
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = client.execute(httpPost);
            if (response != null) {

                String responseBody = EntityUtils.toString(response.getEntity());
                Log.d("response to sync", responseBody);
                Object jsonObj = new JSONTokener(responseBody).nextValue();
                if (jsonObj instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) jsonObj;
                    //further actions on jsonObjects

                } else if (jsonObj instanceof JSONArray) {
                    //further actions on jsonArray
                    JSONArray jsonArray = (JSONArray) jsonObj;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }