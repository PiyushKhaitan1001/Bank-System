package com.bank.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PptxGenerator {

    public static void main(String[] args) {
        String filename = "Apex_Banking_OOP_Presentation.pptx";
        if (args.length > 0) {
            filename = args[0];
        }
        try {
            generatePptx(filename);
            System.out.println("Successfully generated: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generatePptx(String filename) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(filename))) {
            addEntry(zos, "[Content_Types].xml", getContentTypesXml());
            addEntry(zos, "_rels/.rels", getRootRelsXml());
            addEntry(zos, "ppt/_rels/presentation.xml.rels", getPresentationRelsXml());
            addEntry(zos, "ppt/presentation.xml", getPresentationXml());
            addEntry(zos, "ppt/slideMasters/slideMaster1.xml", getSlideMasterXml());
            addEntry(zos, "ppt/slideMasters/_rels/slideMaster1.xml.rels", getSlideMasterRelsXml());
            addEntry(zos, "ppt/slideLayouts/slideLayout1.xml", getSlideLayoutXml());
            addEntry(zos, "ppt/slideLayouts/_rels/slideLayout1.xml.rels", getSlideLayoutRelsXml());
            addEntry(zos, "ppt/theme/theme1.xml", getThemeXml());

            for (int i = 1; i <= 10; i++) {
                addEntry(zos, "ppt/slides/_rels/slide" + i + ".xml.rels", getSlideRelsXml());
                addEntry(zos, "ppt/slides/slide" + i + ".xml", getSlideXml(i));
            }
        }
    }

    private static void addEntry(ZipOutputStream zos, String path, String content) throws IOException {
        ZipEntry entry = new ZipEntry(path);
        zos.putNextEntry(entry);
        zos.write(content.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    private static String getContentTypesXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        sb.append("<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">\n");
        sb.append("  <Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>\n");
        sb.append("  <Default Extension=\"xml\" ContentType=\"application/xml\"/>\n");
        sb.append("  <Override PartName=\"/ppt/presentation.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.presentation.main+xml\"/>\n");
        sb.append("  <Override PartName=\"/ppt/slideMasters/slideMaster1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slideMaster+xml\"/>\n");
        sb.append("  <Override PartName=\"/ppt/slideLayouts/slideLayout1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml\"/>\n");
        sb.append("  <Override PartName=\"/ppt/theme/theme1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.theme+xml\"/>\n");
        for (int i = 1; i <= 10; i++) {
            sb.append("  <Override PartName=\"/ppt/slides/slide").append(i).append(".xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slide+xml\"/>\n");
        }
        sb.append("</Types>");
        return sb.toString();
    }

    private static String getRootRelsXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n"
                + "  <Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"ppt/presentation.xml\"/>\n"
                + "</Relationships>";
    }

    private static String getPresentationRelsXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        sb.append("<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n");
        sb.append("  <Relationship Id=\"rIdMaster\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideMaster\" Target=\"slideMasters/slideMaster1.xml\"/>\n");
        for (int i = 1; i <= 10; i++) {
            sb.append("  <Relationship Id=\"rIdSld").append(i).append("\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/slide\" Target=\"slides/slide").append(i).append(".xml\"/>\n");
        }
        sb.append("</Relationships>");
        return sb.toString();
    }

    private static String getPresentationXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        sb.append("<p:presentation xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">\n");
        sb.append("  <p:sldMasterIdLst><p:sldMasterId id=\"2147483648\" r:id=\"rIdMaster\"/></p:sldMasterIdLst>\n");
        sb.append("  <p:sldIdLst>\n");
        for (int i = 1; i <= 10; i++) {
            sb.append("    <p:sldId id=\"").append(255 + i).append("\" r:id=\"rIdSld").append(i).append("\"/>\n");
        }
        sb.append("  </p:sldIdLst>\n");
        sb.append("  <p:sldSz cx=\"12192000\" cy=\"6858000\" type=\"screen16x9\"/>\n");
        sb.append("  <p:notesSz cx=\"6858000\" cy=\"9144000\"/>\n");
        sb.append("</p:presentation>");
        return sb.toString();
    }

    private static String getSlideMasterRelsXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n"
                + "  <Relationship Id=\"rIdLayout\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideLayout\" Target=\"../slideLayouts/slideLayout1.xml\"/>\n"
                + "  <Relationship Id=\"rIdTheme\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme\" Target=\"../theme/theme1.xml\"/>\n"
                + "</Relationships>";
    }

    private static String getSlideMasterXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<p:sldMaster xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">\n"
                + "  <p:cSld><p:spTree><p:nvGrpSpPr><p:cNvPr id=\"1\" name=\"\"/><p:cNvGrpSpPr/><p:nvPr/></p:nvGrpSpPr><p:grpSpPr><a:xfrm><a:off x=\"0\" y=\"0\"/><a:ext cx=\"0\" cy=\"0\"/><a:chOff x=\"0\" y=\"0\"/><a:chExt cx=\"0\" cy=\"0\"/></a:xfrm></p:grpSpPr></p:spTree></p:cSld>\n"
                + "  <p:clrMap bg1=\"lt1\" tx1=\"dk1\" bg2=\"lt2\" tx2=\"dk2\" accent1=\"accent1\" accent2=\"accent2\" accent3=\"accent3\" accent4=\"accent4\" accent5=\"accent5\" accent6=\"accent6\" hlink=\"hlink\" folHlink=\"folHlink\"/>\n"
                + "  <p:sldLayoutIdLst><p:sldLayoutId id=\"2147483649\" r:id=\"rIdLayout\"/></p:sldLayoutIdLst>\n"
                + "</p:sldMaster>";
    }

    private static String getSlideLayoutRelsXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n"
                + "  <Relationship Id=\"rIdMaster\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideMaster\" Target=\"../slideMasters/slideMaster1.xml\"/>\n"
                + "</Relationships>";
    }

    private static String getSlideLayoutXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<p:sldLayout xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\" type=\"blank\">\n"
                + "  <p:cSld><p:spTree><p:nvGrpSpPr><p:cNvPr id=\"1\" name=\"\"/><p:cNvGrpSpPr/><p:nvPr/></p:nvGrpSpPr><p:grpSpPr><a:xfrm><a:off x=\"0\" y=\"0\"/><a:ext cx=\"0\" cy=\"0\"/><a:chOff x=\"0\" y=\"0\"/><a:chExt cx=\"0\" cy=\"0\"/></a:xfrm></p:grpSpPr></p:spTree></p:cSld>\n"
                + "  <p:clrMapOvr><a:masterClrMapping/></p:clrMapOvr>\n"
                + "</p:sldLayout>";
    }

    private static String getThemeXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<a:theme xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" name=\"Apex Theme\">\n"
                + "  <a:themeElements>\n"
                + "    <a:clrScheme name=\"ApexScheme\">\n"
                + "      <a:dk1><a:srgbClr val=\"0E1726\"/></a:dk1>\n"
                + "      <a:lt1><a:srgbClr val=\"FFFFFF\"/></a:lt1>\n"
                + "      <a:dk2><a:srgbClr val=\"1E293B\"/></a:dk2>\n"
                + "      <a:lt2><a:srgbClr val=\"F8FAFC\"/></a:lt2>\n"
                + "      <a:accent1><a:srgbClr val=\"10B981\"/></a:accent1>\n"
                + "      <a:accent2><a:srgbClr val=\"06B6D4\"/></a:accent2>\n"
                + "      <a:accent3><a:srgbClr val=\"8B5CF6\"/></a:accent3>\n"
                + "      <a:accent4><a:srgbClr val=\"F59E0B\"/></a:accent4>\n"
                + "      <a:accent5><a:srgbClr val=\"EF4444\"/></a:accent5>\n"
                + "      <a:accent6><a:srgbClr val=\"64748B\"/></a:accent6>\n"
                + "      <a:hlink><a:srgbClr val=\"10B981\"/></a:hlink>\n"
                + "      <a:folHlink><a:srgbClr val=\"059669\"/></a:folHlink>\n"
                + "    </a:clrScheme>\n"
                + "    <a:fontScheme name=\"ApexFont\"><a:majorFont><a:latin typeface=\"Segoe UI\"/></a:majorFont><a:minorFont><a:latin typeface=\"Segoe UI\"/></a:minorFont></a:fontScheme>\n"
                + "    <a:fmtScheme name=\"ApexFmt\"><a:fillStyleLst><a:solidFill><a:schemeClr val=\"phClr\"/></a:solidFill></a:fillStyleLst><a:lnStyleLst><a:ln w=\"9525\"><a:solidFill><a:schemeClr val=\"phClr\"/></a:solidFill></a:ln></a:lnStyleLst><a:effectStyleLst><a:effectStyle><a:effectLst/></a:effectStyle></a:effectStyleLst><a:bgFillStyleLst><a:solidFill><a:schemeClr val=\"phClr\"/></a:solidFill></a:bgFillStyleLst></a:fmtScheme>\n"
                + "  </a:themeElements>\n"
                + "</a:theme>";
    }

    private static String getSlideRelsXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n"
                + "  <Relationship Id=\"rIdLayout\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideLayout\" Target=\"../slideLayouts/slideLayout1.xml\"/>\n"
                + "</Relationships>";
    }

    private static String getSlideXml(int slideNum) {
        SlideData data = getSlideData(slideNum);
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        sb.append("<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">\n");
        sb.append("  <p:cSld>\n");
        sb.append("    <p:spTree>\n");
        sb.append("      <p:nvGrpSpPr><p:cNvPr id=\"1\" name=\"\"/><p:cNvGrpSpPr/><p:nvPr/></p:nvGrpSpPr>\n");
        sb.append("      <p:grpSpPr><a:xfrm><a:off x=\"0\" y=\"0\"/><a:ext cx=\"0\" cy=\"0\"/><a:chOff x=\"0\" y=\"0\"/><a:chExt cx=\"0\" cy=\"0\"/></a:xfrm></p:grpSpPr>\n");

        sb.append("      <p:sp>\n");
        sb.append("        <p:nvSpPr><p:cNvPr id=\"2\" name=\"Background\"/><p:cNvSpPr><a:spLocks noGrp=\"1\"/></p:cNvSpPr><p:nvPr/></p:nvSpPr>\n");
        sb.append("        <p:spPr><a:xfrm><a:off x=\"0\" y=\"0\"/><a:ext cx=\"12192000\" cy=\"6858000\"/></a:xfrm><a:prstGeom prst=\"rect\"><a:avLst/></a:prstGeom><a:solidFill><a:srgbClr val=\"0E1726\"/></a:solidFill></p:spPr>\n");
        sb.append("      </p:sp>\n");

        sb.append("      <p:sp>\n");
        sb.append("        <p:nvSpPr><p:cNvPr id=\"3\" name=\"HeaderCard\"/><p:cNvSpPr/><p:nvPr/></p:nvSpPr>\n");
        sb.append("        <p:spPr><a:xfrm><a:off x=\"609600\" y=\"457200\"/><a:ext cx=\"10972800\" cy=\"1143000\"/></a:xfrm><a:prstGeom prst=\"roundRect\"><a:avLst><a:gd name=\"adj\" fmla=\"val 8000\"/></a:avLst></p:prstGeom><a:solidFill><a:srgbClr val=\"142036\"/></a:solidFill><a:ln w=\"19050\"><a:solidFill><a:srgbClr val=\"1E293B\"/></a:solidFill></a:ln></p:spPr>\n");
        sb.append("        <p:txBody><a:bodyPr lIns=\"274320\" tIns=\"137160\" rIns=\"274320\" bIns=\"137160\"/><a:lstStyle/>\n");
        sb.append("          <a:p><a:r><a:rPr sz=\"1300\" b=\"1\"><a:solidFill><a:srgbClr val=\"10B981\"/></a:solidFill></a:rPr><a:t>").append(escapeXml(data.tag)).append("</a:t></a:r></a:p>\n");
        sb.append("          <a:p><a:r><a:rPr sz=\"2200\" b=\"1\"><a:solidFill><a:srgbClr val=\"F8FAFC\"/></a:solidFill></a:rPr><a:t>").append(escapeXml(data.title)).append("</a:t></a:r></a:p>\n");
        sb.append("        </p:txBody>\n");
        sb.append("      </p:sp>\n");

        sb.append("      <p:sp>\n");
        sb.append("        <p:nvSpPr><p:cNvPr id=\"4\" name=\"ContentCard\"/><p:cNvSpPr/><p:nvPr/></p:nvSpPr>\n");
        sb.append("        <p:spPr><a:xfrm><a:off x=\"609600\" y=\"1828800\"/><a:ext cx=\"10972800\" cy=\"4389120\"/></a:xfrm><a:prstGeom prst=\"roundRect\"><a:avLst><a:gd name=\"adj\" fmla=\"val 8000\"/></a:avLst></p:prstGeom><a:solidFill><a:srgbClr val=\"142036\"/></a:solidFill><a:ln w=\"19050\"><a:solidFill><a:srgbClr val=\"1E293B\"/></a:solidFill></a:ln></p:spPr>\n");
        sb.append("        <p:txBody><a:bodyPr lIns=\"365760\" tIns=\"274320\" rIns=\"365760\" bIns=\"274320\"/><a:lstStyle/>\n");

        for (String line : data.points) {
            sb.append("          <a:p><a:pPr marL=\"288000\" indent=\"-288000\"><a:buChar char=\"&#9656;\"/></a:pPr>");
            if (line.startsWith("HEADER:")) {
                String h = line.substring(7);
                sb.append("<a:r><a:rPr sz=\"1600\" b=\"1\"><a:solidFill><a:srgbClr val=\"38BDF8\"/></a:solidFill></a:rPr><a:t>").append(escapeXml(h)).append("</a:t></a:r>");
            } else {
                sb.append("<a:r><a:rPr sz=\"1350\"><a:solidFill><a:srgbClr val=\"F8FAFC\"/></a:solidFill></a:rPr><a:t>").append(escapeXml(line)).append("</a:t></a:r>");
            }
            sb.append("</a:p>\n");
        }

        sb.append("        </p:txBody>\n");
        sb.append("      </p:sp>\n");

        sb.append("      <p:sp>\n");
        sb.append("        <p:nvSpPr><p:cNvPr id=\"5\" name=\"Footer\"/><p:cNvSpPr/><p:nvPr/></p:nvSpPr>\n");
        sb.append("        <p:spPr><a:xfrm><a:off x=\"609600\" y=\"6324600\"/><a:ext cx=\"10972800\" cy=\"381000\"/></a:xfrm><a:prstGeom prst=\"rect\"/></p:spPr>\n");
        sb.append("        <p:txBody><a:bodyPr/><a:lstStyle/><a:p><a:r><a:rPr sz=\"1000\" b=\"1\"><a:solidFill><a:srgbClr val=\"64748B\"/></a:solidFill></a:rPr><a:t>APEX BANKING ENTERPRISE &bull; SECTION T4 &bull; SLIDE ").append(slideNum).append(" OF 10 &bull; OBJECT ORIENTED PROGRAMMING CORE JAVA</a:t></a:r></a:p></p:txBody>\n");
        sb.append("      </p:sp>\n");

        sb.append("    </p:spTree>\n");
        sb.append("  </p:cSld>\n");
        sb.append("  <p:clrMapOvr><a:masterClrMapping/></p:clrMapOvr>\n");
        sb.append("</p:sld>");
        return sb.toString();
    }

    private static String escapeXml(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;");
    }

    private static class SlideData {
        String tag;
        String title;
        String[] points;

        SlideData(String tag, String title, String[] points) {
            this.tag = tag;
            this.title = title;
            this.points = points;
        }
    }

    private static SlideData getSlideData(int index) {
        if (index == 1) {
            return new SlideData(
                    "OBJECT ORIENTED PROGRAMMING • CORE JAVA PROJECT PRESENTATION",
                    "Apex Banking Management System",
                    new String[]{
                            "HEADER:Enterprise Neobank Platform Built Exclusively from First Principles",
                            "Lead Presenter: Piyush Khaitan (Registration No: 2026-CS-0412)",
                            "Team Member: Project Partner (Registration No: 2026-CS-0413)",
                            "Academic Cohort: Section T4 | Date: 27 October 2026",
                            "Strict Constraints: Core Java (JDK 17+) Only | Zero External Libraries | Zero Frameworks | Zero SQL Database",
                            "7-MINUTE TIMED PRESENTATION • USE THIS COMMON FORMAT FOR ALL PROJECTS"
                    }
            );
        }
        if (index == 2) {
            return new SlideData(
                    "01 • PROBLEM STATEMENT & USE CASE",
                    "Connecting Software Solutions to Real User Needs",
                    new String[]{
                            "HEADER:1. What Problem Does the Application Solve?",
                            "Eliminates dependency bloat, high memory overhead, and security vulnerabilities of heavy frameworks (Spring Boot, Hibernate).",
                            "Proves an industrial-grade neobank can be built with 100% Core Java, native HTTP server, custom cryptography, and CSV storage.",
                            "HEADER:2. Intended Users & Realistic Use Case",
                            "Retail Customers (self-service accounts, UPI, transfers) and Bank Staff/Admins (KYC queue, loan disbursal, dispute refunds).",
                            "Realistic Use Case: Customer John transfers ₹1,200 to Alice. System triggers a 2-step PIN check, displays high-value OTP, checks reserve balance, mutates balances atomistically, and logs immutable transactions.",
                            "HEADER:3. Clear Objective Statement",
                            "\"To architect and implement a secure, fully compliant Neobank Management System using strictly Core Java (JDK 17+) and OOP principles, delivering robust transaction processing with zero external dependencies.\""
                    }
            );
        }
        if (index == 3) {
            return new SlideData(
                    "02 • OOP DESIGN & ARCHITECTURE",
                    "Applying Core Object-Oriented Principles",
                    new String[]{
                            "HEADER:UML Class Relationships & Domain Hierarchy",
                            "Abstract Base Class: Account (accountNumber, holderName, balance, nominee, branchCode)",
                            "Inheritance Subclasses: SavingsAccount, CurrentAccount, FixedDepositAccount, RecurringDepositAccount",
                            "Interfaces: BankService (business contract) and Storage (persistence abstraction)",
                            "Core Implementations: BankServiceImpl (singleton service coordinator) and FileStorage (CSV persistence engine)",
                            "HEADER:Application of 4 OOP Pillars",
                            "Abstraction: Account defines abstract calculateInterest(); BankService decouples HTTP layer from business rules.",
                            "Inheritance: Four specialized account classes reuse core balance and nominee behaviors without duplication.",
                            "Polymorphism: Dynamic method dispatch in calculateInterest() calculates 4% p.a., 0% overdraft, compound FD, or RD installments.",
                            "Encapsulation: Strictly private fields across all 15 models with mutating methods deposit() and withdraw() validating minimum reserves."
                    }
            );
        }
        if (index == 4) {
            return new SlideData(
                    "03 • CORE JAVA CONCEPTS USED",
                    "Language Features Powering the Implementation",
                    new String[]{
                            "HEADER:Classes, Modifiers & Custom Exceptions",
                            "Custom OOP Exceptions: InsufficientFundsException, AccountFrozenException, InvalidAmountException, DailyLimitExceededException.",
                            "Access Modifiers & Constructors: Private fields, public APIs, package-private helpers, explicit constructors with sanitization.",
                            "HEADER:Collections, Concurrency & Security",
                            "Collections & Generics: List<Account>, List<Transaction>, Map<String, User>, and ConcurrentHashMap for thread-safe sessions.",
                            "Cryptography: SHA-256 via java.security.MessageDigest for hashing customer passwords and 4-digit transaction PINs.",
                            "HEADER:File I/O & Embedded HTTP Engine",
                            "File I/O: java.io.BufferedReader and BufferedWriter for high-throughput CSV persistence with auto-directory recovery.",
                            "Built-in HTTP Server: com.sun.net.httpserver.HttpServer on port 8080 with custom routing, cookie parsing, and URL decoders.",
                            "Code Style: 0 inline comments, 0 switch statements, 0 ternary operators (? :), modular methods strictly under 30 lines."
                    }
            );
        }
        if (index == 5) {
            return new SlideData(
                    "04 • PROGRAM FLOW & IMPLEMENTATION",
                    "Logic Design, Algorithms & Implementation",
                    new String[]{
                            "HEADER:Atomic Money Transfer Flow",
                            "1. Client POSTs transfer form with sourceAccount, targetAccount, amount, 4-digit PIN, and OTP.",
                            "2. BankServer resolves active session User; delegates to BankServiceImpl.transfer().",
                            "3. Verification Phase: Authenticates PIN hash, verifies simulated OTP if amount > ₹1,000, checks daily cumulative cap (₹5,000).",
                            "4. Account Validation: Verifies both accounts exist, not frozen, and source satisfies minimum reserve (₹500 Savings or Overdraft).",
                            "5. Atomic Execution: source.withdraw(amount) + target.deposit(amount) executed inside synchronized method.",
                            "6. Ledger & Persistence: Appends 2 immutable Transaction records and flushes updated accounts to CSV on disk.",
                            "HEADER:Key Logic Decision & Algorithm",
                            "All preconditions evaluated before any state mutation; failure at any step throws a custom exception leaving balances 100% intact."
                    }
            );
        }
        if (index == 6) {
            return new SlideData(
                    "05 • APPLICATION DEMONSTRATION",
                    "Live Showcase of Working Banking System",
                    new String[]{
                            "HEADER:Feature 1: Customer Neobank Portal & Transfers Hub",
                            "1-Click Live Demo Quick Switcher (toggle Admin, Manager, Teller, John, Alice) & Hero Card with KYC status.",
                            "Transfers Hub with 6 isolated sub-tabs: Direct Transfer, UPI Instant Pay (user@apex), Beneficiaries, Bills, Standing Instructions, FX.",
                            "Live execution of 2-step PIN transfer + simulated on-screen high-value OTP challenge banner.",
                            "HEADER:Feature 2: Cards, Deposits & Customer Hub",
                            "Virtual EMV Debit Card controls with daily spending limit setter and instant 1-click Freeze lock.",
                            "Booking Fixed Deposit vaults with automatic maturity calculation (up to 8.0% p.a.) and nominee registration.",
                            "Customer Hub: Savings goals progress percentage bars and categorized monthly expense breakdown.",
                            "HEADER:Feature 3: Staff Administration Portal (/admin)",
                            "Live SVG Analytics Chart comparing Deposits vs. Withdrawals rendered natively without external JS.",
                            "KYC Document Queue: 1-click Approve/Reject review for submitted identity documents.",
                            "Loan amortization queue with EMI calculator and batch monthly savings interest run."
                    }
            );
        }
        if (index == 7) {
            return new SlideData(
                    "06 • TESTING, RESULTS & LIMITATIONS",
                    "Quality Assurance, Verification & Constraints",
                    new String[]{
                            "HEADER:Test Case 1: Minimum Balance Guard (PASSED)",
                            "Input: Transfer ₹1,100 from Savings ACC1001 (Balance: ₹1,500, Min: ₹500).",
                            "Result: InsufficientFundsException thrown; transfer rejected; ₹1,500 balance remains completely intact.",
                            "HEADER:Test Case 2: Brute-Force Authentication Lockout (PASSED)",
                            "Input: Enter 3 consecutive incorrect passwords on customer account john.",
                            "Result: User account automatically flagged isLocked = true; all further attempts rejected until Admin unlocks.",
                            "HEADER:Test Case 3: High-Value Transfer OTP Challenge (PASSED)",
                            "Input: Submit transfer > ₹1,000 without entering simulated on-screen OTP.",
                            "Result: Transfer paused; OTP challenge banner displayed; entering matching 6-digit code executes safely.",
                            "HEADER:Current Limitations",
                            "CSV File I/O is optimized for single-node applications; not intended for high-concurrency clustered servers.",
                            "In-memory session tokens reset on server restart (all persistent financial data in CSV is permanent)."
                    }
            );
        }
        if (index == 8) {
            return new SlideData(
                    "07 • WRAP-UP & FUTURE ENHANCEMENTS",
                    "Summary of Learning, Outcomes & Takeaway",
                    new String[]{
                            "HEADER:What the Application Achieves",
                            "Engineered a complete, professional, zero-framework Neobank Management System powered entirely by standard Core Java (JDK 17+).",
                            "Delivered multi-tier accounts, atomic 2-step PIN transfers, instant UPI, loan amortization, virtual cards, KYC, and staff administration.",
                            "HEADER:Primary OOP Concept Mastered",
                            "Polymorphic Domain Modeling & Layered Abstraction: Demonstrating how abstract contracts (Account, BankService, Storage) cleanly isolate business logic from presentation and disk serialization.",
                            "HEADER:Realistic Future Enhancements",
                            "JDBC Relational Database: Plug in PostgreSQL or SQLite via the Storage interface without changing business logic.",
                            "TLS/HTTPS & Biometrics: Upgrade server to HttpsServer with SSL/TLS certificates and FIDO2 WebAuthn biometric security.",
                            "HEADER:Final Core Java Takeaway",
                            "\"Core Java's standard library, when paired with disciplined object-oriented architecture, provides all the necessary power to construct secure, resilient, and enterprise-grade financial software from pure first principles.\""
                    }
            );
        }
        if (index == 9) {
            return new SlideData(
                    "PRESENTATION TIMING BREAKDOWN",
                    "Suggested 7-Minute Presentation Flow",
                    new String[]{
                            "HEADER:Pacing & Speaker Timeline (420 Seconds Total)",
                            "1. Problem Statement & Realistic Use Case: 40 sec (Framework bloat, John's transfer scenario, 1-sentence objective)",
                            "2. OOP Design & UML Class Diagram: 60 sec (Account hierarchy, BankService & Storage interfaces, 4 OOP pillars)",
                            "3. Core Java Language Features: 60 sec (Custom exceptions, CSV File I/O, collections, SHA-256 MessageDigest)",
                            "4. Program Flow & Implementation: 60 sec (Atomic transfer sequence, 2-step PIN/OTP algorithm, code snippet)",
                            "5. Live Application Demonstration: 100 sec (Customer login, transfers sub-tabs, open FD, staff portal & SVG chart)",
                            "6. Testing Results & Limitations: 40 sec (Minimum balance exception, account lockout, OTP challenge, limitations)",
                            "7. Conclusion & Takeaway: 20 sec (Summary of outcomes, primary concept mastered, future enhancements)",
                            "Q&A Buffer: 40 sec (Dedicated time for evaluator inquiries and architectural justification)"
                    }
            );
        }
        return new SlideData(
                "FINAL EVALUATION CHECKLIST",
                "Pre-Presentation Readiness Verification",
                new String[]{
                        "HEADER:Technical Readiness Check",
                        "Clean Compilation: Codebase compiles with zero warnings: javac -d out $(find src -name \"*.java\")",
                        "Server Boot: Runs natively on http://localhost:8080 via java -cp out com.bank.Main",
                        "Pre-Seeded Test Credentials Ready: Admin (admin / admin123), Customers (john / password123, alice / password123)",
                        "Backup Visuals: Presentation HTML slide deck and high-res screenshots ready offline.",
                        "HEADER:OOP & Java Mastery Check",
                        "Can explain responsibilities of Account, SavingsAccount, CurrentAccount, FixedDepositAccount, RecurringDepositAccount.",
                        "Can justify why BankService and Storage are interfaces (loose coupling and testability).",
                        "Can demonstrate dynamic method dispatch in calculateInterest() across all account types.",
                        "Can explain custom exception handling (InsufficientFundsException, AccountFrozenException).",
                        "Both team members fully prepared to field questions on backend design, security, and UI rendering."
                }
        );
    }
}
