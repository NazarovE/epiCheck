import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        // Устанавливаем сохраненный язык
        SharedPreferences prefs = newBase.getSharedPreferences("EpiCheckSettings", MODE_PRIVATE);
        String lang = prefs.getString("Language", Locale.getDefault().getLanguage());
        super.attachBaseContext(updateLocale(newBase, lang));
    }

    private Context updateLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        return context.createConfigurationContext(config);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Дополнительные общие настройки для всех Activity
    }
}