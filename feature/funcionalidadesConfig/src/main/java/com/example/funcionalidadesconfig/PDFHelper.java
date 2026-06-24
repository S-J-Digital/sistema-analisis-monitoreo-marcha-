package com.example.funcionalidadesconfig;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;

public class PDFHelper {

    PdfDocument pdfDocument;
    PdfDocument.Page page;
    Canvas canvas;

    Paint textPaint;
    Paint titlePaint;
    Paint linePaint;

    int pageWidth = 612;
    int pageHeight = 1000;
    int margin = 40;

    int currentY;
    int pageNumber = 1;

    String headerTitle;

    PDFHelper(PdfDocument pdfDocument, String headerTitle) {
        this.pdfDocument = pdfDocument;
        this.headerTitle = headerTitle;

        textPaint = new Paint();
        textPaint.setTextSize(12);

        titlePaint = new Paint();
        titlePaint.setTextSize(16);
        titlePaint.setFakeBoldText(true);

        linePaint = new Paint();
        linePaint.setStrokeWidth(2);

        startNewPage();
    }

    private void startNewPage() {

        if (page != null) {
            drawFooter();
            pdfDocument.finishPage(page);
        }

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber++)
                        .create();

        page = pdfDocument.startPage(pageInfo);
        canvas = page.getCanvas();

        currentY = margin;

        drawHeader();
    }

    private void drawHeader() {
        canvas.drawText(headerTitle, margin, currentY, titlePaint);
        currentY += 25;
        canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint);
        currentY += 20;
    }

    private void drawFooter() {
        canvas.drawLine(margin, pageHeight - margin - 10,
                pageWidth - margin, pageHeight - margin - 10, linePaint);

        canvas.drawText("Página " + (pageNumber - 1),
                pageWidth - 120,
                pageHeight - margin,
                textPaint);
    }

    private void checkSpace(int elementHeight) {
        if (currentY + elementHeight > pageHeight - margin - 40) {
            startNewPage();
        }
    }

    void drawSectionTitle(String text) {
        checkSpace(30);
        canvas.drawText(text, margin, currentY, titlePaint);
        currentY += 25;
    }

    void drawText(String text) {
        checkSpace(20);
        canvas.drawText(text, margin, currentY, textPaint);
        currentY += 20;
    }

    // ✅ ESTE ES EL MÉTODO NUEVO
    void drawTwoColumns(String leftText, String rightText) {
        checkSpace(20);

        float columnLeftX = margin;
        float columnRightX = pageWidth / 2f;

        canvas.drawText(leftText, columnLeftX, currentY, textPaint);
        canvas.drawText(rightText, columnRightX, currentY, textPaint);

        currentY += 20;
    }

    void drawLine() {
        checkSpace(20);
        canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint);
        currentY += 20;
    }

    void drawBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }

        checkSpace(bitmap.getHeight() + 20);
        canvas.drawBitmap(bitmap, margin, currentY, null);
        currentY += bitmap.getHeight() + 20;
    }

    void drawTable(String[] headers, String[][] rows) {

        int columnCount = headers.length;
        int tableWidth = pageWidth - (margin * 2);
        int columnWidth = tableWidth / columnCount;
        int rowHeight = 30;

        Paint tablePaint = new Paint();
        tablePaint.setStyle(Paint.Style.STROKE);
        tablePaint.setStrokeWidth(1);

        Paint headerPaint = new Paint();
        headerPaint.setTextSize(12);
        headerPaint.setFakeBoldText(true);

        Paint cellPaint = new Paint();
        cellPaint.setTextSize(11);

        // 🔹 Dibujar encabezados
        checkSpace(rowHeight);

        int startX = margin;
        int startY = currentY;

        for (int i = 0; i < columnCount; i++) {
            canvas.drawRect(
                    startX + (i * columnWidth),
                    startY,
                    startX + ((i + 1) * columnWidth),
                    startY + rowHeight,
                    tablePaint
            );

            canvas.drawText(
                    headers[i],
                    startX + (i * columnWidth) + 10,
                    startY + 20,
                    headerPaint
            );
        }

        currentY += rowHeight;

        // 🔹 Dibujar filas
        for (String[] row : rows) {

            checkSpace(rowHeight);
            startY = currentY;

            for (int i = 0; i < columnCount; i++) {

                canvas.drawRect(
                        startX + (i * columnWidth),
                        startY,
                        startX + ((i + 1) * columnWidth),
                        startY + rowHeight,
                        tablePaint
                );

                canvas.drawText(
                        row[i],
                        startX + (i * columnWidth) + 10,
                        startY + 20,
                        cellPaint
                );
            }

            currentY += rowHeight;
        }

        currentY += 20;
    }

    void finish() {
        if (page != null) {
            drawFooter();
            pdfDocument.finishPage(page);
        }
    }
}

