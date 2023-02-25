package com.example.appfond;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class PDFViewActivity extends AppCompatActivity {

    public static String PDF_FILE_URI;
    private Toolbar tb;
    private Button sendPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        MainActivity.from_add = 1;
        tb = findViewById(R.id.viewPDFToolBar);
        sendPDF = findViewById(R.id.sendPDFButton);
        sendPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Uri screenshotUri = FileProvider.getUriForFile(PDFViewActivity.this, BuildConfig.APPLICATION_ID + ".provider",MainActivity.pdffile);

                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("application/pdf");
                share.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivity(Intent.createChooser(share, "Отправить"));
            }
        });

        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Назад");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PDFView pdfView = findViewById(R.id.pdfView);
        //pdfView.fromUri(Uri.parse(MainActivity.pdffile))
        //pdfView.fromAsset("/storage/emulated/0/Android/data/com.appfond.main/files/temp/1677254947061.pdf")
        pdfView.fromFile(MainActivity.pdffile)
               // .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                // allows to draw something on the current page, usually visible in the middle of the screen
                //.onDraw(onDrawListener)
                // allows to draw something on all pages, separately for every page. Called only for visible pages
                //.onDrawAll(onDrawListener)
                //.onLoad(onLoadCompleteListener) // called after document is loaded and starts to be rendered
                //.onPageChange(onPageChangeListener)
                //.onPageScroll(onPageScrollListener)
                //.onError(onErrorListener)
                //.onPageError(onPageErrorListener)
                //.onRender(onRenderListener) // called after document is rendered for the first time
                // called on single tap, return true if handled, false to toggle scroll handle visibility
                //.onTap(onTapListener)
                //.onLongPress(onLongPressListener)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                //.autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                //.linkHandler(DefaultLinkHandler)
                //.pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                //.fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                //.pageSnap(false) // snap pages to screen boundaries
                //.pageFling(false) // make a fling change only a single page like ViewPager
                //.nightMode(false) // toggle night mode
                .enableAnnotationRendering(true)
                //.onPageChange(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .pageSnap(true)
                .load();

       /* Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.fromFile(MainActivity.pdffile);

        sharingIntent.setType("image/png");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "Share image using"));*/


    }



}