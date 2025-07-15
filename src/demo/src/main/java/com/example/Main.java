// שי עושה את זה ביונירסט על ידי מפה לפרמטרים ורק אובייקט ריספונס של
// יונירסט שמקבל את הלינק ואת הפרמטרים ושולח כגייסון.
// ניתן לראות בשיעור 7 של סדנא בגיטהאב.

// GET = turning the JSON string we get into an object
// POST = turning the object into a JSON String we can post 

//בבקשות פוסט שולחים מידע אל השרת ולכן יש להמיר את האובייקט המקומי למחרוזת גייסון שתואמת למבנה שהשרת מצפה לו ולשלוח אותה כגוף הבקשה.
// לעומת זאת, בבקשת גט מקבלים מהשרת מידע בפורמט גייסון, ויש להמיר את המחרוזת שהתקבלה חזרה לאובייקט תואם בקוד כדי שנוכל לעבוד איתו בצורה נוחה. 
//כלומר, בפוסט עושים המרה מאובייקט לגייסון, ובגט עושים המרה מגייסון לאובייקט.
package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        String apiUrl = ""; // הלינק לשרת שנקבל

        // ניצור אובייקט גייסון עם המידע שנרצה לשלוח לשרת:

        // אם המבנה של השרת זה גייסון רגיל ולא מערך:

        JSONObject json = new JSONObject();
        // לתקן לפי מה שיבקשו:
        json.put("param1", "param1-String-value");
        json.put("param2", 213000000); // כמו ת״ז לדוגמה (הערך שך פרמטר2)
        json.put("param3", "param3-String-value");
        // ...

        // OR:

        // אם המבנה של הגייסון זה מערך ובכל תא פרמטרים:

        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("param1", "param1-String-value");
        jsonObject1.put("param2", 213000000);
        jsonObject1.put("param3", "param3-String-value");
        // ...

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("param1", "param1-String-value");
        jsonObject2.put("param2", 213000000);
        jsonObject2.put("param3", "param3-String-value");
        // ...

        jsonArray.put(jsonObject1);
        jsonArray.put(jsonObject2);
        // ...

        // אם יש לי גייסון מערך של גייסונים מערכים שכל תא מכיל פרמטרים אז בפוסט נשלח את
        // המערך

        // בכללי:
        // אם היינו מגדירים רק אובייקט גייסון עם פרמטרים (כמו בדוגמה למעלה) והיינו
        // שולחים אותו לשרת בצורת מערך אז זה היה מכניס את זה כאיבר למערך. כמו שעשינו עם
        // אביה בתגבור 4 בגיטהאב.
        // ז״א לא צריך להגדיר גם מערך גייסון בשביל זה.

        // נבנה את הבקשה
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json") // להשאיר ככה. נותנים שם לחלק הגלוי בקישור ואת הסוג פורמט
                                                            // (גייסון) שמוצג באינטרנט
                .POST(HttpRequest.BodyPublishers.ofString(json.toString())) // להשאיר ככה רק לשנות את השם של הגייסון
                                                                            // ששולחים. תמיד נשלח כסטרינג.
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Response code: " + response.statusCode()); // נבדוק אם הבקשה הצליחה (200 ומשהו)
        System.out.println("Response body: " + response.body()); // הדפסה לבדיקה

        // אם השרת מחזיר מחרוזת גייסון, אפשר להמיר את התשובה חזרה לאובייקט ולהשתמש במה
        // שהכנסנו לו בבקשת פוסט:

        // אם זה גייסון רגיל
        JSONObject jsonResponse = new JSONObject(response.body());
        Object param = jsonResponse.getObjectType("param-key-name"); // להשלים ולתקן את הטייפ לפי מה שהמפתח מחזיר
        System.out.println(param);
        // ...

        // OR:

        // אם זה מערך:
        JSONArray JsonArray = new JSONArray(response.body());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject JsonObject = jsonArray.getJSONObject(i);
            Object NeededParam = JsonObject.getObjectType("neededParam-String-Key"); // להשלים ולתקן את הטייפ לפי מה
                                                                                     // שהמפתח מחזיר
            // ....
        }

    }
}
