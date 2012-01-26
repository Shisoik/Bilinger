/*
 * Copyright Stanislav Belogolov 2011.
 *
 * This file is a part of Bilinger. Bilinger is free software distributed under
 * the terms of GNU General Public License version 3. You should have received
 * a copy of the GNU General Public License along with Bilinger. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package ru.alepar.bilinger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
  static final String PAGE_HEADER = "<!DOCTYPE html>\n<html><head><meta charset='utf-8'>" +
      "<link rel='stylesheet/less' type='text/css' href='file:///android_res/raw/styles.less'>" +
      "<script src='file:///android_res/raw/less.js' type='text/javascript'></script>" +
      "</head><body>";

  private static final String LOG_TAG = "MainActivity";

  private final Handler handler = new Handler();

  private final List<Pair<String,String>> text = new ArrayList<Pair<String, String>>();

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    final BufferedReader bibook;
    try {
      bibook = new BufferedReader(
          new InputStreamReader(new FileInputStream("/mnt/sdcard/bibooks/sample.txt"), "UTF-8"));
      for (String str; (str = bibook.readLine()) != null;) {
        final String[] sentences = str.split("\t");
        text.add(new Pair<String, String>(sentences[0], sentences[1]));
      }
      bibook.close();
    } catch (FileNotFoundException e) {
      Log.e(LOG_TAG, "File not found.");
    } catch (IOException e) {
      Log.e(LOG_TAG, "IOException: " + e.getMessage());
    }
  }

  @Override
  protected void onStart() {
    super.onStart();

    // Prepare the page.
    final StringBuilder pageBuffer = new StringBuilder(PAGE_HEADER);
    int i = 0;
    for(Pair<String,String> pair : text) {
      if (i == 4)
        addSentence(pageBuffer, pair.second, String.valueOf(i));
      else
        addSentence(pageBuffer, pair.first, String.valueOf(i));
      i++;
    }
    pageBuffer.append("</body></html>");

    // Render it.
    final WebView webView = (WebView) findViewById(R.id.visible_page);
    webView.setWebChromeClient(new LogWebChromeClient());
    webView.addJavascriptInterface(new JavaScriptInterface(), "activity");
    final WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webView.loadDataWithBaseURL("file:///android_res/raw/", pageBuffer.toString(), "text/html", "UTF-8", null);
  }

  private static void addSentence(StringBuilder pageBuffer, String sentence, String id) {
    pageBuffer.append("<a onClick='window.activity.selectSentence(\"").append(id).append("\")'>")
        .append(sentence).append("</a> ");
  }

  /**
   * Provides a hook for calling "alert" from javascript. Useful for
   * debugging your javascript.
   */
  final class LogWebChromeClient extends WebChromeClient {
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
      Log.d(LOG_TAG, message);
      result.confirm();
      return true;
    }
  }
  
  final class JavaScriptInterface {
    @SuppressWarnings({"UnusedDeclaration"})
    public void selectSentence(String id) {
      final int str_id = Integer.parseInt(id);
      handler.post( new Runnable() {
        @Override
        public void run() {
          if (str_id < 0 ||  str_id >= text.size()) {
            Log.e(LOG_TAG, "Sentence id outside of text size.");
          }
          final Pair<String, String> pair = text.get(str_id);
          new SentenceDialogFragment(pair.first, pair.second).show(getFragmentManager(),"dialog");
        }
      });
    }
  }
}
