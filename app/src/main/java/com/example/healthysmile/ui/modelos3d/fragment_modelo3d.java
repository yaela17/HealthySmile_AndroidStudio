package com.example.healthysmile.ui.modelos3d;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthysmile.R;

public class fragment_modelo3d extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_modelo3d, container, false);

        // Configura el WebView
        configureWebView(view);

        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configureWebView(View view) {
        // Obtén el WebView del layout
        WebView webView = view.findViewById(R.id.modelWebView);

        // Configura el WebView
        webView.loadUrl("file:///android_asset/modelViewer.html");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
    }
}
