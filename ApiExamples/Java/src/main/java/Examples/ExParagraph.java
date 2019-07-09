package Examples;

//////////////////////////////////////////////////////////////////////////
// Copyright (c) 2001-2019 Aspose Pty Ltd. All Rights Reserved.
//
// This file is part of Aspose.Words. The source code in this file
// is only intended as a supplement to the documentation, and is provided
// "as is", without warranty of any kind, either expressed or implied.
//////////////////////////////////////////////////////////////////////////

import com.aspose.words.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class ExParagraph extends ApiExampleBase {
    @Test
    public void insertField() throws Exception {
        //ExStart
        //ExFor:Paragraph.AppendField(FieldType, Boolean)
        //ExFor:Paragraph.AppendField(String)
        //ExFor:Paragraph.AppendField(String, String)
        //ExFor:Paragraph.InsertField(string, Node, bool)
        //ExFor:Paragraph.InsertField(FieldType, bool, Node, bool)
        //ExFor:Paragraph.InsertField(string, string, Node, bool)
        //ExSummary:Shows how to insert fields in different ways.
        // Create a blank document and get its first paragraph
        Document doc = new Document();

        Paragraph para = doc.getFirstSection().getBody().getFirstParagraph();

        // Choose a field by FieldType, append it to the end of the paragraph and update it
        para.appendField(FieldType.FIELD_DATE, true);

        // Append a field with a field code created by hand
        para.appendField(" TIME  \\@ \"HH:mm:ss\" ");

        // Append a field that will display a placeholder value until it is updated manually in Microsoft Word
        // or programmatically with Document.UpdateFields() or Field.Update()
        para.appendField(" QUOTE \"Real value\"", "Placeholder value");

        // We can choose a node in the paragraph and insert a field
        // before or after that node instead of appending it to the end of a paragraph
        para = doc.getFirstSection().getBody().appendParagraph("");
        Run run = new Run(doc);
        {
            run.setText(" My Run. ");
        }
        para.appendChild(run);

        // Insert a field into the paragraph and place it before the run we created
        doc.getBuiltInDocumentProperties().get("Author").setValue("John Doe");
        para.insertField(FieldType.FIELD_AUTHOR, true, run, false);

        // Insert another field designated by field code before the run
        para.insertField(" QUOTE \"Real value\" ", run, false);

        // Insert another field with a place holder value and place it after the run
        para.insertField(" QUOTE \"Real value\"", " Placeholder value. ", run, true);

        doc.save(getArtifactsDir() + "Paragraph.InsertField.docx");
        //ExEnd
    }

    @Test
    public void insertFieldBeforeTextInParagraph() throws Exception {
        Document doc = DocumentHelper.createDocumentFillWithDummyText();

        insertFieldUsingFieldCode(doc, " AUTHOR ", null, false, 1);

        Assert.assertEquals(DocumentHelper.getParagraphText(doc, 1), "\u0013 AUTHOR \u0014Test Author\u0015Hello World!\r");
    }

    @Test
    public void insertFieldAfterTextInParagraph() throws Exception {
        LocalDateTime ldt = LocalDateTime.now();
        String date = DateTimeFormatter.ofPattern("M/d/yyyy", Locale.ENGLISH).format(ldt);

        Document doc = DocumentHelper.createDocumentFillWithDummyText();

        insertFieldUsingFieldCode(doc, " DATE ", null, true, 1);

        Assert.assertEquals(DocumentHelper.getParagraphText(doc, 1), MessageFormat.format("Hello World!\u0013 DATE \u0014{0}\u0015\r", date));
    }

    @Test
    public void insertFieldBeforeTextInParagraphWithoutUpdateField() throws Exception {
        Document doc = DocumentHelper.createDocumentFillWithDummyText();

        insertFieldUsingFieldType(doc, FieldType.FIELD_AUTHOR, false, null, false, 1);

        Assert.assertEquals(DocumentHelper.getParagraphText(doc, 1), "\u0013 AUTHOR \u0014\u0015Hello World!\r");
    }

    @Test
    public void insertFieldAfterTextInParagraphWithoutUpdateField() throws Exception {
        Document doc = DocumentHelper.createDocumentFillWithDummyText();

        insertFieldUsingFieldType(doc, FieldType.FIELD_AUTHOR, false, null, true, 1);

        Assert.assertEquals(DocumentHelper.getParagraphText(doc, 1), "Hello World!\u0013 AUTHOR \u0014\u0015\r");
    }

    @Test
    public void insertFieldWithoutSeparator() throws Exception {
        Document doc = DocumentHelper.createDocumentFillWithDummyText();

        insertFieldUsingFieldType(doc, FieldType.FIELD_LIST_NUM, true, null, false, 1);

        Assert.assertEquals(DocumentHelper.getParagraphText(doc, 1), "\u0013 LISTNUM \u0015Hello World!\r");
    }

    @Test
    public void insertFieldBeforeParagraphWithoutDocumentAuthor() throws Exception {
        Document doc = DocumentHelper.createDocumentFillWithDummyText();
        doc.getBuiltInDocumentProperties().setAuthor("");

        insertFieldUsingFieldCodeFieldString(doc, " AUTHOR ", null, null, false, 1);

        Assert.assertEquals(DocumentHelper.getParagraphText(doc, 1), "\u0013 AUTHOR \u0014\u0015Hello World!\r");
    }

    @Test
    public void insertFieldAfterParagraphWithoutChangingDocumentAuthor() throws Exception {
        Document doc = DocumentHelper.createDocumentFillWithDummyText();

        insertFieldUsingFieldCodeFieldString(doc, " AUTHOR ", null, null, true, 1);

        Assert.assertEquals(DocumentHelper.getParagraphText(doc, 1), "Hello World!\u0013 AUTHOR \u0014\u0015\r");
    }

    @Test
    public void insertFieldBeforeRunText() throws Exception {
        Document doc = DocumentHelper.createDocumentFillWithDummyText();

        // Add some text into the paragraph
        Run run = DocumentHelper.insertNewRun(doc, " Hello World!", 1);

        insertFieldUsingFieldCodeFieldString(doc, " AUTHOR ", "Test Field Value", run, false, 1);

        Assert.assertEquals(DocumentHelper.getParagraphText(doc, 1), "Hello World!\u0013 AUTHOR \u0014Test Field Value\u0015 Hello World!\r");
    }

    @Test
    public void insertFieldAfterRunText() throws Exception {
        Document doc = DocumentHelper.createDocumentFillWithDummyText();

        // Add some text into the paragraph
        Run run = DocumentHelper.insertNewRun(doc, " Hello World!", 1);

        insertFieldUsingFieldCodeFieldString(doc, " AUTHOR ", "", run, true, 1);

        Assert.assertEquals(DocumentHelper.getParagraphText(doc, 1), "Hello World! Hello World!\u0013 AUTHOR \u0014\u0015\r");
    }

    @Test(description = "WORDSNET-12396")
    public void insertFieldEmptyParagraphWithoutUpdateField() throws Exception {
        Document doc = DocumentHelper.createDocumentWithoutDummyText();

        insertFieldUsingFieldType(doc, FieldType.FIELD_AUTHOR, false, null, false, 1);

        Assert.assertEquals(DocumentHelper.getParagraphText(doc, 1), "\u0013 AUTHOR \u0014\u0015\f");
    }

    @Test(description = "WORDSNET-12397")
    public void insertFieldEmptyParagraphWithUpdateField() throws Exception {
        Document doc = DocumentHelper.createDocumentWithoutDummyText();

        insertFieldUsingFieldType(doc, FieldType.FIELD_AUTHOR, true, null, false, 0);

        Assert.assertEquals(DocumentHelper.getParagraphText(doc, 0), "\u0013 AUTHOR \u0014Test Author\u0015\r");
    }

    @Test
    public void getFormatRevision() throws Exception {
        //ExStart
        //ExFor:Paragraph.IsFormatRevision
        //ExSummary:Shows how to get information about whether this object was formatted in Microsoft Word while change tracking was enabled
        Document doc = new Document(getMyDir() + "Paragraph.IsFormatRevision.docx");

        Paragraph firstParagraph = DocumentHelper.getParagraph(doc, 0);
        Assert.assertTrue(firstParagraph.isFormatRevision());
        //ExEnd

        Paragraph secondParagraph = DocumentHelper.getParagraph(doc, 1);
        Assert.assertFalse(secondParagraph.isFormatRevision());
    }

    @Test
    public void getFrameProperties() throws Exception {
        //ExStart
        //ExFor:Paragraph.FrameFormat
        //ExFor:FrameFormat
        //ExFor:FrameFormat.IsFrame
        //ExFor:FrameFormat.Width
        //ExFor:FrameFormat.Height
        //ExFor:FrameFormat.HeightRule
        //ExFor:FrameFormat.HorizontalAlignment
        //ExFor:FrameFormat.VerticalAlignment
        //ExFor:FrameFormat.HorizontalPosition
        //ExFor:FrameFormat.RelativeHorizontalPosition
        //ExFor:FrameFormat.HorizontalDistanceFromText
        //ExFor:FrameFormat.VerticalPosition
        //ExFor:FrameFormat.RelativeVerticalPosition
        //ExFor:FrameFormat.VerticalDistanceFromText
        //ExSummary:Shows how to get information about formatting properties of paragraph as frame.
        Document doc = new Document(getMyDir() + "Paragraph.Frame.docx");

        ParagraphCollection paragraphs = doc.getFirstSection().getBody().getParagraphs();

        for (Paragraph paragraph : (Iterable<Paragraph>) paragraphs) {
            if (paragraph.getFrameFormat().isFrame()) {
                System.out.println("Width: " + paragraph.getFrameFormat().getWidth());
                System.out.println("Height: " + paragraph.getFrameFormat().getHeight());
                System.out.println("HeightRule: " + paragraph.getFrameFormat().getHeightRule());
                System.out.println("HorizontalAlignment: " + paragraph.getFrameFormat().getHorizontalAlignment());
                System.out.println("VerticalAlignment: " + paragraph.getFrameFormat().getVerticalAlignment());
                System.out.println("HorizontalPosition: " + paragraph.getFrameFormat().getHorizontalPosition());
                System.out.println("RelativeHorizontalPosition: " + paragraph.getFrameFormat().getRelativeHorizontalPosition());
                System.out.println("HorizontalDistanceFromText: " + paragraph.getFrameFormat().getHorizontalDistanceFromText());
                System.out.println("VerticalPosition: " + paragraph.getFrameFormat().getVerticalPosition());
                System.out.println("RelativeVerticalPosition: " + paragraph.getFrameFormat().getRelativeVerticalPosition());
                System.out.println("VerticalDistanceFromText: " + paragraph.getFrameFormat().getVerticalDistanceFromText());
            }
        }
        //ExEnd

        if (paragraphs.get(0).getFrameFormat().isFrame()) {
            Assert.assertEquals(paragraphs.get(0).getFrameFormat().getWidth(), 233.3);
            Assert.assertEquals(paragraphs.get(0).getFrameFormat().getHeight(), 138.8);
            Assert.assertEquals(paragraphs.get(0).getFrameFormat().getHorizontalPosition(), 21.05);
            Assert.assertEquals(paragraphs.get(0).getFrameFormat().getRelativeHorizontalPosition(), RelativeHorizontalPosition.PAGE);
            Assert.assertEquals(paragraphs.get(0).getFrameFormat().getHorizontalDistanceFromText(), 9.0);
            Assert.assertEquals(paragraphs.get(0).getFrameFormat().getVerticalPosition(), -17.65);
            Assert.assertEquals(paragraphs.get(0).getFrameFormat().getRelativeVerticalPosition(), RelativeVerticalPosition.PARAGRAPH);
            Assert.assertEquals(paragraphs.get(0).getFrameFormat().getVerticalDistanceFromText(), 0.0);
        } else {
            Assert.fail("There are no frames in the document.");
        }
    }

    @Test
    public void asianTypographyProperties() throws Exception {
        //ExStart
        //ExFor:ParagraphFormat.FarEastLineBreakControl
        //ExFor:ParagraphFormat.WordWrap
        //ExFor:ParagraphFormat.HangingPunctuation
        //ExSummary:Shows how to set special properties for Asian typography. 
        Document doc = new Document(getMyDir() + "Document.docx");

        ParagraphFormat format = doc.getFirstSection().getBody().getParagraphs().get(0).getParagraphFormat();
        format.setFarEastLineBreakControl(true);
        format.setWordWrap(false);
        format.setHangingPunctuation(true);

        doc.save(getArtifactsDir() + "Paragraph.AsianTypographyProperties.docx");
        //ExEnd
    }

    @Test
    public void dropCapPosition() throws Exception {
        //ExStart
        //ExFor:DropCapPosition
        //ExSummary:Shows how to set the position of a drop cap.
        // Create a blank document
        Document doc = new Document();

        // Every paragraph has its own drop cap setting
        Paragraph para = doc.getFirstSection().getBody().getFirstParagraph();

        // By default, it is "none", for no drop caps
        Assert.assertEquals(para.getParagraphFormat().getDropCapPosition(), com.aspose.words.DropCapPosition.NONE);

        // Move the first capital to outside the text margin
        para.getParagraphFormat().setDropCapPosition(com.aspose.words.DropCapPosition.MARGIN);

        // This text will be affected
        para.getRuns().add(new Run(doc, "Hello World!"));

        doc.save(getArtifactsDir() + "Paragraph.DropCap.docx");
        //ExEnd
    }

    /**
     * Insert field into the first paragraph of the current document using field type
     */
    private static void insertFieldUsingFieldType(final Document doc, final int fieldType, final boolean updateField,
                                                  final Node refNode, final boolean isAfter, final int paraIndex) throws Exception {
        Paragraph para = DocumentHelper.getParagraph(doc, paraIndex);
        para.insertField(fieldType, updateField, refNode, isAfter);
    }

    /**
     * Insert field into the first paragraph of the current document using field code
     */
    private static void insertFieldUsingFieldCode(final Document doc, final String fieldCode, final Node refNode,
                                                  final boolean isAfter, final int paraIndex) throws Exception {
        Paragraph para = DocumentHelper.getParagraph(doc, paraIndex);
        para.insertField(fieldCode, refNode, isAfter);
    }

    /**
     * Insert field into the first paragraph of the current document using field code and field String
     */
    private static void insertFieldUsingFieldCodeFieldString(final Document doc, final String fieldCode,
                                                             final String fieldValue, final Node refNode, final boolean isAfter, int paraIndex) {
        Paragraph para = DocumentHelper.getParagraph(doc, paraIndex);
        para.insertField(fieldCode, fieldValue, refNode, isAfter);
    }

    @Test
    public void isRevision() throws Exception {
        //ExStart
        //ExFor:Paragraph.IsDeleteRevision
        //ExFor:Paragraph.IsInsertRevision
        //ExSummary:Shows how to work with revision paragraphs.
        // Create a blank document, populate the first paragraph with text and add two more
        Document doc = new Document();
        Body body = doc.getFirstSection().getBody();
        Paragraph para = body.getFirstParagraph();
        para.appendChild(new Run(doc, "Paragraph 1. "));
        body.appendParagraph("Paragraph 2. ");
        body.appendParagraph("Paragraph 3. ");

        // We have three paragraphs, none of which registered as any type of revision
        // If we add/remove any content in the document while tracking revisions,
        // they will be displayed as such in the document and can be accepted/rejected
        doc.startTrackRevisions("John Doe", new Date());

        // This paragraph is a revision and will have the according "IsInsertRevision" flag set
        para = body.appendParagraph("Paragraph 4. ");
        Assert.assertTrue(para.isInsertRevision());

        // Get the document's paragraph collection and remove a paragraph
        ParagraphCollection paragraphs = body.getParagraphs();
        Assert.assertEquals(paragraphs.getCount(), 4);
        para = paragraphs.get(2);
        para.remove();

        // Since we are tracking revisions, the paragraph still exists in the document, will have the "IsDeleteRevision" set
        // and will be displayed as a revision in Microsoft Word, until we accept or reject all revisions
        Assert.assertEquals(paragraphs.getCount(), 4);
        Assert.assertTrue(para.isDeleteRevision());

        // The delete revision paragraph is removed once we accept changes
        doc.acceptAllRevisions();
        Assert.assertEquals(paragraphs.getCount(), 3);
        Assert.assertEquals(para.getCount(), 0);
        //ExEnd
    }

    @Test
    public void breakIsStyleSeparator() throws Exception {
        //ExStart
        //ExFor:Paragraph.BreakIsStyleSeparator
        //ExSummary:Shows how to write text to the same line as a TOC heading and have it not show up in the TOC.
        // Create a blank document and insert a table of contents field
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);
        builder.insertTableOfContents("\\o \\h \\z \\u");
        builder.insertBreak(BreakType.PAGE_BREAK);

        // Insert a paragraph with a style that will be picked up as an entry in the TOC
        builder.getParagraphFormat().setStyleIdentifier(StyleIdentifier.HEADING_1);

        // Both these strings are on the same line and same paragraph and will therefore show up on the same TOC entry
        builder.write("Heading 1. ");
        builder.write("Will appear in the TOC. ");

        // Any text on a new line that does not have a heading style will not register as a TOC entry
        // If we insert a style separator, we can write more text on the same line
        // and use a different style without it showing up in the TOC
        // If we use a heading type style afterwards, we can draw two TOC entries from one line of document text
        builder.insertStyleSeparator();
        builder.getParagraphFormat().setStyleIdentifier(StyleIdentifier.QUOTE);
        builder.write("Won't appear in the TOC. ");

        // This flag is set to true for such paragraphs
        Assert.assertTrue(doc.getFirstSection().getBody().getParagraphs().get(0).getBreakIsStyleSeparator());

        // Update the TOC and save the document
        doc.updateFields();
        doc.save(getArtifactsDir() + "Paragraph.BreakIsStyleSeparator.docx");
        //ExEnd
    }

    @Test
    public void tabStops() throws Exception {
        //ExStart
        //ExFor:Paragraph.GetEffectiveTabStops
        //ExSummary:Shows how to set custom tab stops.
        // Create a blank document and get the first paragraph
        Document doc = new Document();
        Paragraph para = doc.getFirstSection().getBody().getFirstParagraph();

        // If there are no tab stops in this collection, while we are in this paragraph
        // the cursor will jump 36 points each time we press the Tab key in Microsoft Word
        Assert.assertEquals(para.getEffectiveTabStops().length, 0);

        // We can add custom tab stops in Microsoft Word if we enable the ruler via the view tab
        // Each unit on that ruler is two default tab stops, which is 72 points
        // Those tab stops can be programmatically added to the paragraph like this
        para.getParagraphFormat().getTabStops().add(72.0, TabAlignment.LEFT, TabLeader.DOTS);
        para.getParagraphFormat().getTabStops().add(216.0, TabAlignment.CENTER, TabLeader.DASHES);
        para.getParagraphFormat().getTabStops().add(360.0, TabAlignment.RIGHT, TabLeader.LINE);

        // These tab stops are added to this collection, and can also be seen by enabling the ruler mentioned above
        Assert.assertEquals(para.getEffectiveTabStops().length, 3);

        // Add a Run with tab characters that will snap the text to our TabStop positions and save the document
        para.appendChild(new Run(doc, "\tTab 1\tTab 2\tTab 3"));
        doc.save(getArtifactsDir() + "Paragraph.TabStops.docx");
        //ExEnd
    }

    @Test
    public void joinRuns() throws Exception {
        //ExStart
        //ExFor:Paragraph.JoinRunsWithSameFormatting
        //ExSummary:Shows how to simplify paragraphs by merging superfluous runs.
        // Create a blank Document and insert a few short Runs into the first Paragraph
        // Having many small runs with the same formatting can happen if, for instance,
        // we edit a document extensively in Microsoft Word
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);
        builder.write("Run 1. ");
        builder.write("Run 2. ");
        builder.write("Run 3. ");
        builder.write("Run 4. ");

        // The Paragraph may look like it's in once piece in Microsoft Word,
        // but under the surface it is fragmented into several Runs, which leaves room for optimization
        Paragraph para = builder.getCurrentParagraph();
        Assert.assertEquals(para.getRuns().getCount(), 4);

        // Change the style of the last run to something different from the first three
        para.getRuns().get(3).getFont().setStyleIdentifier(StyleIdentifier.EMPHASIS);

        // We can run the JoinRunsWithSameFormatting() method to merge similar Runs
        // This method also returns the number of joins that occured during the merge
        // Two merges occured to combine Runs 1-3, while Run 4 was left out because it has an incompatible style
        Assert.assertEquals(para.joinRunsWithSameFormatting(), 2);

        // The paragraph has been simplified to two runs
        Assert.assertEquals(para.getRuns().getCount(), 2);
        Assert.assertEquals(para.getRuns().get(0).getText(), "Run 1. Run 2. Run 3. ");
        Assert.assertEquals(para.getRuns().get(1).getText(), "Run 4. ");
        //ExEnd
    }
}
