import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.FileInputStream;
import java.io.IOException;

public class PdfTree {

    // Working copy for drawing tree on multiple pages and wrapping up the long text
    public static void main(String[] args) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            PDType0Font font = PDType0Font.load(document, new FileInputStream("U:\\Java Projects\\Roboto-Light.ttf"));
            document.addPage(page);

            float pageHeight = page.getMediaBox().getHeight() - 50;  // Adjust for margins
            float bottomMargin = 50;
            float pageWidth = page.getMediaBox().getWidth();

            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true, true);
            contentStream.setFont(font, 12);

            // Starting coordinates for the root node
            float startX = 50;
            float startY = 700;
            //float startY = pageHeight;
            // Define indent and base spacing
            float ownerIndent, docIndent, actPDFIndent;
            ownerIndent = docIndent = actPDFIndent = 50;
            float baseSpacing = 30;

            // Define page break limits

            // Define number of Items to show
            int numOwnerTypes = 3;
            int numDocTypes = 2;
            int numActualPdf = 5;

            // Draw root node
            String rootText = "RootFile.pdf";
            drawText(startX, startY, rootText, contentStream,font,pageWidth-startX);

            // Append OwnerTypes under each Root
            float currentY = startY;
            float rootTextWidth = font.getStringWidth(rootText) / 1000 * 12;
            //float rootMiddleX = startX + (rootTextWidth) / 2;
            float rootMiddleX = startX + 2;
            float ownerX = rootMiddleX + ownerIndent;

            // Track vertical lines spanning pages
            float lastVerticalLineStartY = currentY;  // Track for vertical line

            for (int i = 0; i < numOwnerTypes; i++) {
                String ownerTypeText = "OwnerType" + (i + 1) + ".pdf";
                // Page break check
                System.out.println(i+" :"+ownerTypeText);
                if ((currentY - baseSpacing) < bottomMargin) {
                    // Draw the vertical line to the bottom of the page before breaking
                    drawVerticalLine(rootMiddleX, lastVerticalLineStartY, bottomMargin, contentStream);

                    // Move to the next page
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true, true);
                    contentStream.setFont(font, 12);

                    // Reset Y coordinate and continue the vertical line on the new page
                    currentY = pageHeight;
                    lastVerticalLineStartY = pageHeight;
                }


                currentY -= baseSpacing;
                drawHorizontalLine(rootMiddleX, (ownerX - 2), (currentY + 4), contentStream);
                drawText(ownerX, currentY, ownerTypeText, contentStream, font, pageWidth - ownerX);

                if (i == numOwnerTypes - 1) {
                    drawVerticalLine(rootMiddleX, lastVerticalLineStartY, currentY, contentStream);
                }

                // Append DocType under each OwnerType
                float ownerTextWidth = font.getStringWidth(ownerTypeText) / 1000 * 12;
                //float ownerMiddleX = ownerX + ownerTextWidth / 2;  // Middle of the OwnerType node
                float ownerMiddleX = ownerX + 2;
                float docX = ownerMiddleX + docIndent;
                float lastOwnerY = currentY;

                for (int j = 0; j < numDocTypes; j++) {
                    // Page break check
                    if (currentY - baseSpacing < bottomMargin) {

                        // Drawing vertical line from Root node
                        if(i!=numOwnerTypes - 1){
                            drawVerticalLine(rootMiddleX, lastVerticalLineStartY, bottomMargin, contentStream);
                        }
                        drawVerticalLine(ownerMiddleX, lastOwnerY, bottomMargin, contentStream);

                        // Move to the next page
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true, true);
                        contentStream.setFont(font, 12);

                        // Reset Y coordinate and continue the vertical line on the new page
                        currentY = pageHeight;
                        lastOwnerY = pageHeight;
                        lastVerticalLineStartY = pageHeight;
                    }

                    String docTypeText = "DocType" + (j + 1) + ".pdf";
                    currentY -= baseSpacing;
                    drawHorizontalLine(ownerMiddleX, (docX - 2), (currentY + 4), contentStream);
                    drawText(docX, currentY, docTypeText, contentStream, font, pageWidth - ownerX);

                    if (j == numDocTypes - 1) {
                        drawVerticalLine(ownerMiddleX, lastOwnerY, currentY, contentStream);
                    }

                    // Append actual pdf under each DocType
                    float docTextWidth = font.getStringWidth(docTypeText) / 1000 * 12;
                    //float docMiddleX = docX + docTextWidth / 2;
                    float docMiddleX = docX + 2;
                    float actPdfX = docMiddleX + actPDFIndent;
                    float lastDocY = currentY;

                    for (int k = 0; k < numActualPdf; k++) {
                        // Page break check
                        String actualPdfText1 = "actualpdf_dfdfdfdf_dfdfdfdfdfdf_sdfkdfdkfjdfkj_fdkfdfjdfkjsdkjfjf_12dfdfdfdfdfdfdfdfdfdfdfdf_dfdfdhfdjhfdjfdhjf-dfdfdfdfdfff-fdfdfdfdf" + (k + 1) + ".pdf";
                        float requiredTotalHeight = predictTextHeight(actualPdfText1,font,pageWidth-actPdfX,15);
                        if ((currentY-(requiredTotalHeight+baseSpacing))<bottomMargin || currentY - baseSpacing < bottomMargin) {

                            // Drawing vertical line from Root
                            if(i!=numOwnerTypes - 1){
                                drawVerticalLine(rootMiddleX, lastVerticalLineStartY, bottomMargin, contentStream);
                            }

                            // Drawing vertical line from OwnerType
                            if(j!=numDocTypes-1){
                                drawVerticalLine(ownerMiddleX, lastOwnerY, bottomMargin, contentStream);
                            }
                            drawVerticalLine(docMiddleX, lastDocY, bottomMargin, contentStream);

                            // Move to the next page
                            contentStream.close();
                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true, true);
                            contentStream.setFont(font, 12);

                            // Reset Y coordinate and continue the vertical line on the new page
                            currentY = pageHeight;
                            lastDocY = pageHeight;
                            lastOwnerY = pageHeight;
                            lastVerticalLineStartY = pageHeight;
                        }

                        currentY -= baseSpacing;
                        // Normal horizontal line for ActualPDF - at start of line
                        //drawHorizontalLine(docMiddleX, (actPdfX - 2), (currentY + 4), contentStream);

                        // Bracketed horizontal line for ActualPDF
                        // Normal horizontal line for ActualPDF - at middle of all text
                        drawHorizontalLine(docMiddleX, (actPdfX - 2), (currentY - (requiredTotalHeight/2)+4), contentStream);
                        drawVerticalLine((actPdfX - 2),currentY+10,(currentY-requiredTotalHeight)-4,contentStream);
                        drawHorizontalLine((actPdfX - 2),(actPdfX - 2)+3,currentY+10,contentStream);
                        drawHorizontalLine((actPdfX - 2),(actPdfX - 2)+3,(currentY-requiredTotalHeight)-4,contentStream);
                        float newY = drawText(actPdfX, currentY, actualPdfText1, contentStream, font, pageWidth - actPdfX);

                        if (k == numActualPdf - 1) {
                            drawVerticalLine(docMiddleX, lastDocY, (currentY - (requiredTotalHeight/2)+4), contentStream);
                        }
                        currentY = newY;
                    }
                }
            }

            // Save and close the document
            contentStream.close();
            document.save("U:\\TreeStructure.pdf");
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void drawVerticalLine(float commonX, float y1, float y2,PDPageContentStream contentStream) throws IOException{
        contentStream.moveTo(commonX, y1);
        contentStream.lineTo(commonX, y2);
        contentStream.stroke();
    }

    private static void drawHorizontalLine(float x1,float x2, float commonY,PDPageContentStream contentStream) throws IOException{
        contentStream.moveTo(x1, commonY);
        contentStream.lineTo(x2, commonY);
        contentStream.stroke();
    }

    private static float drawText(float x, float y, String text, PDPageContentStream contentStream, PDType0Font font, float maxWidth) throws IOException {
        int textLength = text.length();
        int start = 0;
        float originalY = y;
        System.out.println("1:y:"+y);
        while (start < textLength) {
            // Find the maximum number of characters that fit into the maxWidth
            int end = start;
            float width = 0;

            // Measure each character to see how much can fit within maxWidth
            while (end < textLength) {
                char currentChar = text.charAt(end);
                width += (font.getStringWidth(String.valueOf(currentChar)) / 1000 * 12);
                if (width+50 > maxWidth) {
                    break;
                }
                end++;
            }

            // Draw the substring that fits
            contentStream.beginText();
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(text.substring(start, end));
            contentStream.endText();

            // Move to the next line
            y -= 15;

            // Update the start index for the next substring
            start = end;
        }

        return y; // Return the updated y position
    }

    private static float predictTextHeight(String text, PDType0Font font, float maxWidth, float lineHeight) throws IOException {
        int textLength = text.length();
        int start = 0;
        float totalHeight = 0;

        while (start < textLength) {
            // Initialize width for each line
            float lineWidth = 0;
            int end = start;

            // Calculate the width of each character and find how many can fit in the maxWidth
            while (end < textLength) {
                char currentChar = text.charAt(end);
                float charWidth = font.getStringWidth(String.valueOf(currentChar)) / 1000 * 12;
                if (lineWidth + charWidth > maxWidth) {
                    break;
                }
                lineWidth += charWidth;
                end++;
            }

            // Count one line of text
            totalHeight += lineHeight;

            // Move the start pointer to the next part of the text
            start = end;
        }

        return totalHeight;
    }

}
