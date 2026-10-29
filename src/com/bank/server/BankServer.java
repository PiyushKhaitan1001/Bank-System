package com.bank.server;

import com.bank.model.Account;
import com.bank.model.AccountFrozenException;
import com.bank.model.AuditLog;
import com.bank.model.Beneficiary;
import com.bank.model.Branch;
import com.bank.model.Card;
import com.bank.model.ChequeBookRequest;
import com.bank.model.DailyLimitExceededException;
import com.bank.model.Employee;
import com.bank.model.Feedback;
import com.bank.model.InsufficientFundsException;
import com.bank.model.InvalidAmountException;
import com.bank.model.Loan;
import com.bank.model.Notification;
import com.bank.model.SavingsGoal;
import com.bank.model.StandingInstruction;
import com.bank.model.SupportTicket;
import com.bank.model.Transaction;
import com.bank.model.UpiProfile;
import com.bank.model.User;
import com.bank.service.BankService;
import com.bank.ui.PageBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class BankServer implements HttpHandler {
    private final int port;
    private final BankService bankService;
    private final SessionManager sessionManager;
    private HttpServer server;

    public BankServer(int port, BankService bankService, SessionManager sessionManager) {
        this.port = port;
        this.bankService = bankService;
        this.sessionManager = sessionManager;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", this);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI());

        if (path.equals("/style.css")) {
            sendCssResponse(exchange, PageBuilder.getCss());
            return;
        }

        if (path.equals("/login")) {
            handleLogin(exchange, method, queryParams);
            return;
        }

        if (path.equals("/register")) {
            handleRegister(exchange, method, queryParams);
            return;
        }

        if (path.equals("/reset-password")) {
            handleResetPassword(exchange, method, queryParams);
            return;
        }

        if (path.equals("/logout")) {
            handleLogout(exchange);
            return;
        }

        if (path.equals("/demo/switch")) {
            handleDemoSwitch(exchange, queryParams);
            return;
        }

        if (path.equals("/demo/reset")) {
            handleDemoReset(exchange);
            return;
        }

        User user = getAuthenticatedUser(exchange);
        if (user == null) {
            sendRedirect(exchange, "/login");
            return;
        }

        if (path.equals("/") || path.equals("/dashboard")) {
            handleDashboard(exchange, user, queryParams);
            return;
        }

        if (path.equals("/accounts")) {
            handleAccounts(exchange, user, queryParams);
            return;
        }

        if (path.equals("/accounts/new")) {
            handleOpenAccount(exchange, user, method, queryParams);
            return;
        }

        if (path.equals("/accounts/fd")) {
            handleOpenFd(exchange, user, method, queryParams);
            return;
        }

        if (path.equals("/accounts/rd")) {
            handleOpenRd(exchange, user, method, queryParams);
            return;
        }

        if (path.equals("/transfers")) {
            handleTransfersHub(exchange, user, queryParams);
            return;
        }

        if (path.equals("/transfers/send")) {
            handleSendTransfer(exchange, user);
            return;
        }

        if (path.equals("/transfers/upi")) {
            handleUpiTransfer(exchange, user);
            return;
        }

        if (path.equals("/upi/limit")) {
            handleUpiLimit(exchange, user);
            return;
        }

        if (path.equals("/deposit")) {
            handleDeposit(exchange, user, method, queryParams);
            return;
        }

        if (path.equals("/withdraw")) {
            handleWithdraw(exchange, user, method, queryParams);
            return;
        }

        if (path.equals("/bills/pay")) {
            handlePayBill(exchange, user);
            return;
        }

        if (path.equals("/beneficiaries/add")) {
            handleAddBeneficiary(exchange, user);
            return;
        }

        if (path.equals("/beneficiaries/remove")) {
            handleRemoveBeneficiary(exchange, user);
            return;
        }

        if (path.equals("/standing/new")) {
            handleNewStanding(exchange, user);
            return;
        }

        if (path.equals("/standing/cancel")) {
            handleCancelStanding(exchange, user);
            return;
        }

        if (path.equals("/cards")) {
            handleCards(exchange, user, queryParams);
            return;
        }

        if (path.equals("/cards/limit")) {
            handleCardLimit(exchange, user);
            return;
        }

        if (path.equals("/cards/toggle")) {
            handleCardToggle(exchange, user);
            return;
        }

        if (path.equals("/cheques/request")) {
            handleChequeRequest(exchange, user);
            return;
        }

        if (path.equals("/loans")) {
            handleLoans(exchange, user, queryParams);
            return;
        }

        if (path.equals("/loans/apply")) {
            handleApplyLoan(exchange, user);
            return;
        }

        if (path.equals("/loans/repay")) {
            handleRepayLoan(exchange, user);
            return;
        }

        if (path.equals("/statements")) {
            handleStatements(exchange, user, queryParams);
            return;
        }

        if (path.equals("/statement/download")) {
            handleDownloadStatement(exchange, user);
            return;
        }

        if (path.equals("/disputes/raise")) {
            handleRaiseDispute(exchange, user);
            return;
        }

        if (path.equals("/hub")) {
            handleHub(exchange, user, queryParams);
            return;
        }

        if (path.equals("/profile/update")) {
            handleUpdateProfile(exchange, user);
            return;
        }

        if (path.equals("/profile/password")) {
            handleUpdatePassword(exchange, user);
            return;
        }

        if (path.equals("/kyc/submit")) {
            handleSubmitKyc(exchange, user);
            return;
        }

        if (path.equals("/goals/new")) {
            handleNewGoal(exchange, user);
            return;
        }

        if (path.equals("/tickets/new")) {
            handleNewTicket(exchange, user);
            return;
        }

        if (path.equals("/feedback/submit")) {
            handleSubmitFeedback(exchange, user);
            return;
        }

        if (path.startsWith("/admin")) {
            handleAdminSection(exchange, user, path, method, queryParams);
            return;
        }

        sendRedirect(exchange, "/dashboard");
    }

    private void handleLogin(HttpExchange exchange, String method, Map<String, String> params) throws IOException {
        if (method.equalsIgnoreCase("GET")) {
            User existing = getAuthenticatedUser(exchange);
            if (existing != null) {
                sendRedirect(exchange, "/dashboard");
                return;
            }
            sendHtmlResponse(exchange, 200, PageBuilder.buildLoginPage(params.get("error"), params.get("success")));
            return;
        }

        if (method.equalsIgnoreCase("POST")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            User u = bankService.authenticate(form.get("username"), form.get("password"));
            if (u != null) {
                String sid = sessionManager.createSession(u.getUsername());
                sendRedirectWithCookie(exchange, "/dashboard", sid);
            } else {
                User check = bankService.getUserByUsername(form.get("username"));
                if (check != null && check.isLocked()) {
                    sendRedirect(exchange, "/login?error=Account+locked+due+to+3+failed+attempts.+Use+Forgot+Password+or+contact+Admin.");
                } else {
                    sendRedirect(exchange, "/login?error=Invalid+username+or+password");
                }
            }
        }
    }

    private void handleRegister(HttpExchange exchange, String method, Map<String, String> params) throws IOException {
        if (method.equalsIgnoreCase("GET")) {
            sendHtmlResponse(exchange, 200, PageBuilder.buildRegisterPage(params.get("error"), params.get("success")));
            return;
        }

        if (method.equalsIgnoreCase("POST")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            String u = form.get("username");
            String p = form.get("password");
            String cp = form.get("confirmPassword");
            String pin = form.get("pin");
            String cpin = form.get("confirmPin");

            if (u == null || u.trim().isEmpty() || p == null || p.trim().isEmpty()) {
                sendRedirect(exchange, "/register?error=Missing+credentials");
                return;
            }
            if (!p.equals(cp)) {
                sendRedirect(exchange, "/register?error=Passwords+do+not+match");
                return;
            }
            if (pin == null || pin.length() != 4 || !pin.equals(cpin)) {
                sendRedirect(exchange, "/register?error=Invalid+or+mismatched+4-digit+PIN");
                return;
            }

            boolean ok = bankService.register(u, p, form.get("email"), form.get("phone"), form.get("address"), pin);
            if (ok) {
                sendRedirect(exchange, "/login?success=Registration+successful.+Sign+in+now.");
            } else {
                sendRedirect(exchange, "/register?error=Username+already+taken");
            }
        }
    }

    private void handleResetPassword(HttpExchange exchange, String method, Map<String, String> params) throws IOException {
        if (method.equalsIgnoreCase("GET")) {
            sendHtmlResponse(exchange, 200, PageBuilder.buildResetPasswordPage(params.get("error"), params.get("success")));
            return;
        }
        if (method.equalsIgnoreCase("POST")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            boolean ok = bankService.resetPasswordWithSecurityQuestion(form.get("username"), form.get("securityAnswer"), form.get("newPassword"));
            if (ok) {
                sendRedirect(exchange, "/login?success=Password+reset+and+account+unlocked.+Please+sign+in.");
            } else {
                sendRedirect(exchange, "/reset-password?error=Incorrect+security+answer+or+user+not+found.");
            }
        }
    }

    private void handleLogout(HttpExchange exchange) throws IOException {
        String sid = sessionManager.getSessionIdFromExchange(exchange);
        if (sid != null) {
            sessionManager.invalidateSession(sid);
        }
        exchange.getResponseHeaders().set("Set-Cookie", "session_id=; Path=/; Max-Age=0; HttpOnly");
        sendRedirect(exchange, "/login?success=Logged+out+successfully");
    }

    private void handleDemoSwitch(HttpExchange exchange, Map<String, String> params) throws IOException {
        String targetUsername = params.get("user");
        if (targetUsername == null || targetUsername.trim().isEmpty()) {
            sendRedirect(exchange, "/login");
            return;
        }
        User target = bankService.getUserByUsername(targetUsername.trim());
        if (target == null) {
            sendRedirect(exchange, "/dashboard?error=Demo+user+not+found");
            return;
        }
        String oldSession = sessionManager.getSessionIdFromExchange(exchange);
        if (oldSession != null) {
            sessionManager.invalidateSession(oldSession);
        }
        String sid = sessionManager.createSession(target.getUsername());
        sendRedirectWithCookie(exchange, "/dashboard?success=Switched+to+" + target.getUsername() + "+(" + target.getRole() + ")", sid);
    }

    private void handleDemoReset(HttpExchange exchange) throws IOException {
        bankService.resetDemoData();
        String oldSession = sessionManager.getSessionIdFromExchange(exchange);
        if (oldSession != null) {
            sessionManager.invalidateSession(oldSession);
        }
        String sid = sessionManager.createSession("admin");
        sendRedirectWithCookie(exchange, "/dashboard?success=Demo+state+reset+to+default+evaluation+state", sid);
    }

    private void handleDashboard(HttpExchange exchange, User user, Map<String, String> params) throws IOException {
        List<Account> accounts = bankService.getAccountsByUsername(user.getUsername());
        List<Transaction> txns = bankService.getTransactionsByUser(user.getUsername());
        List<Card> cards = bankService.getCardsByUsername(user.getUsername());
        UpiProfile upi = bankService.getUpiProfileByUsername(user.getUsername());
        List<Loan> loans = bankService.getLoansByUsername(user.getUsername());
        List<Notification> notifs = bankService.getNotificationsByUsername(user.getUsername());
        String html = PageBuilder.buildDashboardPage(user, accounts, txns, cards, upi, loans, notifs, params.get("error"), params.get("success"));
        sendHtmlResponse(exchange, 200, html);
    }

    private void handleAccounts(HttpExchange exchange, User user, Map<String, String> params) throws IOException {
        List<Account> accounts = bankService.getAccountsByUsername(user.getUsername());
        List<Branch> branches = bankService.getAllBranches();
        String html = PageBuilder.buildAccountsPage(user, accounts, branches, params.get("error"), params.get("success"));
        sendHtmlResponse(exchange, 200, html);
    }

    private void handleOpenAccount(HttpExchange exchange, User user, String method, Map<String, String> params) throws IOException {
        List<Branch> branches = bankService.getAllBranches();
        if (method.equalsIgnoreCase("GET")) {
            sendHtmlResponse(exchange, 200, PageBuilder.buildOpenAccountPage(user, branches, params.get("error"), params.get("success")));
            return;
        }
        if (method.equalsIgnoreCase("POST")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            double dep = Double.parseDouble(form.get("initialDeposit"));
            Account a = bankService.openAccountWithDetails(user.getUsername(), form.get("accountType"), dep,
                    form.get("jointHolder"), form.get("nomineeName"), form.get("nomineeRelation"), form.get("branchCode"), true);
            if (a != null) {
                sendRedirect(exchange, "/accounts?success=Account+" + a.getAccountNumber() + "+created");
            } else {
                sendRedirect(exchange, "/accounts/new?error=Opening+failed.+Check+initial+deposit+rules.");
            }
        }
    }

    private void handleOpenFd(HttpExchange exchange, User user, String method, Map<String, String> params) throws IOException {
        List<Branch> branches = bankService.getAllBranches();
        if (method.equalsIgnoreCase("GET")) {
            sendHtmlResponse(exchange, 200, PageBuilder.buildFdPage(user, branches, params.get("error"), params.get("success")));
            return;
        }
        if (method.equalsIgnoreCase("POST")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            double principal = Double.parseDouble(form.get("principal"));
            int term = Integer.parseInt(form.get("termMonths"));
            Account fd = bankService.openFixedDeposit(user.getUsername(), principal, term, form.get("nomineeName"), form.get("nomineeRelation"), "APEX0001");
            if (fd != null) {
                sendRedirect(exchange, "/accounts?success=Fixed+Deposit+" + fd.getAccountNumber() + "+booked");
            } else {
                sendRedirect(exchange, "/accounts/fd?error=FD+booking+failed.+Ensure+you+have+sufficient+liquid+funds+above+₹1000.");
            }
        }
    }

    private void handleOpenRd(HttpExchange exchange, User user, String method, Map<String, String> params) throws IOException {
        List<Branch> branches = bankService.getAllBranches();
        if (method.equalsIgnoreCase("GET")) {
            sendHtmlResponse(exchange, 200, PageBuilder.buildRdPage(user, branches, params.get("error"), params.get("success")));
            return;
        }
        if (method.equalsIgnoreCase("POST")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            double install = Double.parseDouble(form.get("monthlyInstallment"));
            int term = Integer.parseInt(form.get("termMonths"));
            Account rd = bankService.openRecurringDeposit(user.getUsername(), install, term, form.get("nomineeName"), form.get("nomineeRelation"), "APEX0001");
            if (rd != null) {
                sendRedirect(exchange, "/accounts?success=Recurring+Deposit+" + rd.getAccountNumber() + "+started");
            } else {
                sendRedirect(exchange, "/accounts/rd?error=RD+failed.+Check+first+installment+balance.");
            }
        }
    }

    private void handleTransfersHub(HttpExchange exchange, User user, Map<String, String> params) throws IOException {
        List<Account> accounts = bankService.getAccountsByUsername(user.getUsername());
        List<Beneficiary> beneficiaries = bankService.getBeneficiariesByUsername(user.getUsername());
        List<StandingInstruction> siList = bankService.getStandingInstructionsByUsername(user.getUsername());
        UpiProfile upi = bankService.getUpiProfileByUsername(user.getUsername());
        String html = PageBuilder.buildTransfersHubPage(user, accounts, beneficiaries, siList, upi, params.get("error"), params.get("success"), params.get("simOtp"));
        sendHtmlResponse(exchange, 200, html);
    }

    private void handleSendTransfer(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        String src = form.get("sourceAccount");
        String tgt = form.get("targetAccount");
        double amt = Double.parseDouble(form.get("amount"));
        String pin = form.get("pin");
        String otp = form.get("otp");

        if (amt > 1000.0) {
            if (otp == null || !bankService.verifyOtp(user.getUsername(), otp)) {
                String genOtp = bankService.generateOtp(user.getUsername());
                sendRedirect(exchange, "/transfers?simOtp=" + genOtp + "&error=High-value+transfer+requires+OTP.+Enter+the+code+displayed+above.");
                return;
            }
        }

        try {
            boolean ok = bankService.transfer(src, tgt, amt, pin);
            if (ok) {
                sendRedirect(exchange, "/transfers?success=Transferred+₹" + String.format("%.2f", amt) + "+to+" + tgt);
            } else {
                sendRedirect(exchange, "/transfers?error=Transfer+declined.+Check+PIN+or+account+numbers.");
            }
        } catch (InsufficientFundsException | AccountFrozenException | InvalidAmountException | DailyLimitExceededException e) {
            sendRedirect(exchange, "/transfers?error=" + PageBuilder.escapeHtml(e.getMessage()).replace(" ", "+"));
        }
    }

    private void handleUpiTransfer(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        String targetUpi = form.get("targetUpi");
        double amt = Double.parseDouble(form.get("amount"));
        String pin = form.get("pin");
        try {
            boolean ok = bankService.processUpiPayment(user.getUsername(), targetUpi, amt, pin);
            if (ok) {
                sendRedirect(exchange, "/transfers?success=UPI+Payment+of+₹" + String.format("%.2f", amt) + "+sent+to+" + targetUpi);
            } else {
                sendRedirect(exchange, "/transfers?error=UPI+Transfer+declined.+Check+PIN+or+UPI+ID.");
            }
        } catch (InsufficientFundsException | AccountFrozenException | InvalidAmountException | DailyLimitExceededException e) {
            sendRedirect(exchange, "/transfers?error=" + PageBuilder.escapeHtml(e.getMessage()).replace(" ", "+"));
        }
    }

    private void handleUpiLimit(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        String upiId = form.get("upiId");
        double limit = Double.parseDouble(form.get("dailyLimit"));
        bankService.updateUpiLimit(upiId, limit);
        sendRedirect(exchange, "/transfers?success=UPI+Daily+Limit+updated+to+₹" + String.format("%.2f", limit));
    }

    private void handleDeposit(HttpExchange exchange, User user, String method, Map<String, String> params) throws IOException {
        List<Account> accounts = bankService.getAccountsByUsername(user.getUsername());
        if (method.equalsIgnoreCase("GET")) {
            sendHtmlResponse(exchange, 200, PageBuilder.buildDepositPage(user, accounts, params.get("account"), params.get("error"), params.get("success")));
            return;
        }
        if (method.equalsIgnoreCase("POST")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            try {
                boolean ok = bankService.deposit(form.get("accountNumber"), Double.parseDouble(form.get("amount")), form.get("pin"));
                if (ok) {
                    sendRedirect(exchange, "/dashboard?success=Deposit+completed");
                } else {
                    sendRedirect(exchange, "/deposit?error=Deposit+declined.+Check+PIN.");
                }
            } catch (InsufficientFundsException | AccountFrozenException | InvalidAmountException e) {
                sendRedirect(exchange, "/deposit?error=" + PageBuilder.escapeHtml(e.getMessage()).replace(" ", "+"));
            }
        }
    }

    private void handleWithdraw(HttpExchange exchange, User user, String method, Map<String, String> params) throws IOException {
        List<Account> accounts = bankService.getAccountsByUsername(user.getUsername());
        if (method.equalsIgnoreCase("GET")) {
            sendHtmlResponse(exchange, 200, PageBuilder.buildWithdrawPage(user, accounts, params.get("account"), params.get("error"), params.get("success")));
            return;
        }
        if (method.equalsIgnoreCase("POST")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            try {
                boolean ok = bankService.withdraw(form.get("accountNumber"), Double.parseDouble(form.get("amount")), form.get("pin"));
                if (ok) {
                    sendRedirect(exchange, "/dashboard?success=Withdrawal+completed");
                } else {
                    sendRedirect(exchange, "/withdraw?error=Withdrawal+declined.+Check+PIN.");
                }
            } catch (InsufficientFundsException | AccountFrozenException | InvalidAmountException | DailyLimitExceededException e) {
                sendRedirect(exchange, "/withdraw?error=" + PageBuilder.escapeHtml(e.getMessage()).replace(" ", "+"));
            }
        }
    }

    private void handlePayBill(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        try {
            boolean ok = bankService.payBill(user.getUsername(), form.get("accountNumber"), form.get("billerType"), form.get("billerRef"), Double.parseDouble(form.get("amount")), form.get("pin"));
            if (ok) {
                sendRedirect(exchange, "/transfers?success=Utility+bill+paid+successfully");
            } else {
                sendRedirect(exchange, "/transfers?error=Bill+payment+failed.+Check+PIN.");
            }
        } catch (InsufficientFundsException | AccountFrozenException | InvalidAmountException e) {
            sendRedirect(exchange, "/transfers?error=" + PageBuilder.escapeHtml(e.getMessage()).replace(" ", "+"));
        }
    }

    private void handleAddBeneficiary(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        bankService.addBeneficiary(user.getUsername(), form.get("payeeName"), form.get("payeeAccount"), form.get("nickname"));
        sendRedirect(exchange, "/transfers?success=Beneficiary+saved");
    }

    private void handleRemoveBeneficiary(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        bankService.removeBeneficiary(form.get("id"), user.getUsername());
        sendRedirect(exchange, "/transfers?success=Beneficiary+removed");
    }

    private void handleNewStanding(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        bankService.createStandingInstruction(user.getUsername(), form.get("source"), form.get("target"), Double.parseDouble(form.get("amount")), form.get("frequency"));
        sendRedirect(exchange, "/transfers?success=Standing+instruction+scheduled");
    }

    private void handleCancelStanding(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        bankService.cancelStandingInstruction(form.get("id"), user.getUsername());
        sendRedirect(exchange, "/transfers?success=Standing+instruction+cancelled");
    }

    private void handleCards(HttpExchange exchange, User user, Map<String, String> params) throws IOException {
        List<Card> cards = bankService.getCardsByUsername(user.getUsername());
        List<ChequeBookRequest> cheques = bankService.getChequeRequestsByUsername(user.getUsername());
        List<Account> accounts = bankService.getAccountsByUsername(user.getUsername());
        String html = PageBuilder.buildCardsPage(user, cards, cheques, accounts, params.get("error"), params.get("success"));
        sendHtmlResponse(exchange, 200, html);
    }

    private void handleCardLimit(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        bankService.updateCardLimit(form.get("cardNumber"), Double.parseDouble(form.get("dailyLimit")));
        sendRedirect(exchange, "/cards?success=Card+limit+updated");
    }

    private void handleCardToggle(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        bankService.toggleCardStatus(form.get("cardNumber"));
        sendRedirect(exchange, "/cards?success=Card+status+toggled");
    }

    private void handleChequeRequest(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        bankService.requestChequeBook(user.getUsername(), form.get("accountNumber"), Integer.parseInt(form.get("leaves")));
        sendRedirect(exchange, "/cards?success=Chequebook+requested");
    }

    private void handleLoans(HttpExchange exchange, User user, Map<String, String> params) throws IOException {
        List<Loan> loans = bankService.getLoansByUsername(user.getUsername());
        List<Account> accounts = bankService.getAccountsByUsername(user.getUsername());
        String html = PageBuilder.buildLoansPage(user, loans, accounts, params.get("error"), params.get("success"));
        sendHtmlResponse(exchange, 200, html);
    }

    private void handleApplyLoan(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        Loan l = bankService.applyLoan(user.getUsername(), form.get("loanType"), Double.parseDouble(form.get("principal")), Integer.parseInt(form.get("termMonths")), form.get("disbursedAccount"));
        if (l != null) {
            sendRedirect(exchange, "/loans?success=Loan+application+" + l.getId() + "+submitted");
        } else {
            sendRedirect(exchange, "/loans?error=Application+failed");
        }
    }

    private void handleRepayLoan(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        try {
            boolean ok = bankService.repayLoanEmi(form.get("loanId"), form.get("fromAccount"), form.get("pin"));
            if (ok) {
                sendRedirect(exchange, "/loans?success=EMI+repayment+processed");
            } else {
                sendRedirect(exchange, "/loans?error=EMI+repayment+declined.+Check+PIN.");
            }
        } catch (InsufficientFundsException | AccountFrozenException | InvalidAmountException e) {
            sendRedirect(exchange, "/loans?error=" + PageBuilder.escapeHtml(e.getMessage()).replace(" ", "+"));
        }
    }

    private void handleStatements(HttpExchange exchange, User user, Map<String, String> params) throws IOException {
        List<Transaction> txns = bankService.getTransactionsByUserAndFilter(user.getUsername(), params.get("type"), params.get("category"), params.get("start"), params.get("end"));
        String html = PageBuilder.buildStatementsPage(user, txns, params.get("type"), params.get("category"), params.get("start"), params.get("end"), params.get("error"), params.get("success"));
        sendHtmlResponse(exchange, 200, html);
    }

    private void handleDownloadStatement(HttpExchange exchange, User user) throws IOException {
        List<Transaction> txns = bankService.getTransactionsByUser(user.getUsername());
        StringBuilder csv = new StringBuilder("TxnID,Type,Amount,Source,Target,Category,Timestamp\n");
        for (Transaction t : txns) {
            csv.append(t.getId()).append(",").append(t.getType()).append(",").append(t.getAmount()).append(",")
                    .append(t.getSource()).append(",").append(t.getTarget()).append(",").append(t.getCategory()).append(",")
                    .append(t.getTimestamp()).append("\n");
        }
        byte[] bytes = csv.toString().getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/csv");
        exchange.getResponseHeaders().set("Content-Disposition", "attachment; filename=\"statement_" + user.getUsername() + ".csv\"");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void handleRaiseDispute(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        bankService.disputeTransaction(form.get("id"), user.getUsername());
        sendRedirect(exchange, "/statements?success=Dispute+claim+opened");
    }

    private void handleHub(HttpExchange exchange, User user, Map<String, String> params) throws IOException {
        List<SavingsGoal> goals = bankService.getGoalsByUsername(user.getUsername());
        List<SupportTicket> tickets = bankService.getTicketsByUsername(user.getUsername());
        List<Notification> notifs = bankService.getNotificationsByUsername(user.getUsername());
        List<Transaction> txns = bankService.getTransactionsByUser(user.getUsername());
        Map<String, Double> spending = new HashMap<>();
        for (Transaction t : txns) {
            if (!t.getType().equalsIgnoreCase("DEPOSIT") && !t.getType().equalsIgnoreCase("INTEREST_CREDIT")) {
                double curr = spending.getOrDefault(t.getCategory(), 0.0);
                spending.put(t.getCategory(), curr + t.getAmount());
            }
        }
        String html = PageBuilder.buildHubPage(user, goals, tickets, notifs, spending, params.get("error"), params.get("success"));
        sendHtmlResponse(exchange, 200, html);
    }

    private void handleUpdateProfile(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        bankService.updateProfile(user.getUsername(), form.get("email"), form.get("phone"), form.get("address"));
        sendRedirect(exchange, "/hub?success=Profile+updated");
    }

    private void handleUpdatePassword(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        boolean ok = bankService.changePassword(user.getUsername(), form.get("oldPassword"), form.get("newPassword"));
        if (ok) {
            sendRedirect(exchange, "/hub?success=Password+changed");
        } else {
            sendRedirect(exchange, "/hub?error=Current+password+incorrect");
        }
    }

    private void handleSubmitKyc(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        bankService.submitKyc(user.getUsername(), form.get("documentName"));
        sendRedirect(exchange, "/hub?success=KYC+document+submitted");
    }

    private void handleNewGoal(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        bankService.createGoal(user.getUsername(), form.get("title"), Double.parseDouble(form.get("target")), form.get("targetDate"));
        sendRedirect(exchange, "/hub?success=Savings+goal+created");
    }

    private void handleNewTicket(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        bankService.createTicket(user.getUsername(), form.get("subject"), form.get("description"));
        sendRedirect(exchange, "/hub?success=Support+ticket+submitted");
    }

    private void handleSubmitFeedback(HttpExchange exchange, User user) throws IOException {
        Map<String, String> form = parseFormData(readRequestBody(exchange));
        bankService.submitFeedback(user.getUsername(), Integer.parseInt(form.get("rating")), form.get("comment"));
        sendRedirect(exchange, "/hub?success=Thank+you+for+your+feedback");
    }

    private void handleAdminSection(HttpExchange exchange, User user, String path, String method, Map<String, String> params) throws IOException {
        if (!user.isStaff()) {
            sendRedirect(exchange, "/dashboard?error=Access+denied.+Staff+privileges+required.");
            return;
        }

        if (path.equals("/admin/users/new")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            bankService.createUserByStaff(form.get("username"), form.get("password"), form.get("role"),
                    form.get("email"), form.get("phone"), form.get("address"), form.get("pin"), user.getUsername());
            sendRedirect(exchange, "/admin?tab=users&success=User+created+successfully");
            return;
        }

        if (path.equals("/admin/lock")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            bankService.lockUser(form.get("username"), user.getUsername());
            sendRedirect(exchange, "/admin?tab=users&success=User+locked");
            return;
        }

        if (path.equals("/admin/unlock")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            bankService.unlockUser(form.get("username"), user.getUsername());
            sendRedirect(exchange, "/admin?tab=users&success=User+unlocked");
            return;
        }

        if (path.equals("/admin/accounts/approve")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            bankService.approveAccount(form.get("accountNumber"), user.getUsername());
            sendRedirect(exchange, "/admin?tab=approvals&success=Account+approved");
            return;
        }

        if (path.equals("/admin/accounts/reject")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            bankService.rejectAccount(form.get("accountNumber"), user.getUsername());
            sendRedirect(exchange, "/admin?tab=approvals&success=Account+rejected");
            return;
        }

        if (path.equals("/admin/kyc/review")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            boolean app = Boolean.parseBoolean(form.get("approve"));
            bankService.reviewKyc(form.get("username"), app, user.getUsername());
            sendRedirect(exchange, "/admin?tab=approvals&success=KYC+reviewed");
            return;
        }

        if (path.equals("/admin/loans/review")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            boolean app = Boolean.parseBoolean(form.get("approve"));
            bankService.reviewLoan(form.get("loanId"), app, user.getUsername());
            sendRedirect(exchange, "/admin?tab=loans&success=Loan+reviewed");
            return;
        }

        if (path.equals("/admin/disputes/review")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            boolean app = Boolean.parseBoolean(form.get("approve"));
            bankService.reviewDispute(form.get("id"), app, user.getUsername());
            sendRedirect(exchange, "/admin?tab=loans&success=Dispute+processed");
            return;
        }

        if (path.equals("/admin/branches/new")) {
            Map<String, String> form = parseFormData(readRequestBody(exchange));
            bankService.addBranch(form.get("code"), form.get("name"), form.get("city"), form.get("manager"), user.getUsername());
            sendRedirect(exchange, "/admin?tab=branches&success=Branch+added");
            return;
        }

        if (path.equals("/admin/interest")) {
            int credited = bankService.creditMonthlyInterestToAllSavings();
            sendRedirect(exchange, "/admin?tab=overview&success=Credited+monthly+interest+to+" + credited + "+accounts");
            return;
        }

        String tab = params.getOrDefault("tab", "overview");
        List<User> users = bankService.getAllUsers();
        List<Account> accounts = bankService.getAllAccounts();
        List<Transaction> txns = bankService.getAllTransactions();
        List<Loan> loans = bankService.getAllLoans();
        List<Branch> branches = bankService.getAllBranches();
        List<Employee> employees = bankService.getAllEmployees();
        List<AuditLog> audit = bankService.getAllAuditLogs();
        List<Transaction> fraud = bankService.getFraudAlerts();
        List<ChequeBookRequest> cheques = bankService.getAllChequeRequests();
        List<SupportTicket> tickets = bankService.getAllTickets();

        String html = PageBuilder.buildAdminDashboard(user, users, accounts, txns, loans, branches, employees, audit, fraud, cheques, tickets, tab, params.get("error"), params.get("success"));
        sendHtmlResponse(exchange, 200, html);
    }

    private User getAuthenticatedUser(HttpExchange exchange) {
        String sessionId = sessionManager.getSessionIdFromExchange(exchange);
        if (sessionId == null) {
            return null;
        }
        String username = sessionManager.getUsername(sessionId);
        if (username == null) {
            return null;
        }
        return bankService.getUserByUsername(username);
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String lengthHeader = exchange.getRequestHeaders().getFirst("Content-Length");
        if (lengthHeader != null) {
            try {
                int length = Integer.parseInt(lengthHeader.trim());
                if (length > 0) {
                    byte[] data = is.readNBytes(length);
                    return new String(data, StandardCharsets.UTF_8);
                } else {
                    return "";
                }
            } catch (NumberFormatException ignored) {
            }
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;
        while (is.available() > 0 && (nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private Map<String, String> parseFormData(String body) {
        Map<String, String> map = new HashMap<>();
        if (body == null || body.trim().isEmpty()) {
            return map;
        }
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                map.put(decodeUrl(kv[0]), decodeUrl(kv[1]));
            } else {
                if (kv.length == 1) {
                    map.put(decodeUrl(kv[0]), "");
                }
            }
        }
        return map;
    }

    private Map<String, String> parseQueryParams(URI uri) {
        Map<String, String> map = new HashMap<>();
        String query = uri.getRawQuery();
        if (query == null || query.trim().isEmpty()) {
            return map;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                map.put(decodeUrl(kv[0]), decodeUrl(kv[1]));
            } else {
                if (kv.length == 1) {
                    map.put(decodeUrl(kv[0]), "");
                }
            }
        }
        return map;
    }

    private String decodeUrl(String val) {
        if (val == null) {
            return "";
        }
        try {
            return URLDecoder.decode(val, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return val;
        }
    }

    private void sendHtmlResponse(HttpExchange exchange, int statusCode, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void sendCssResponse(HttpExchange exchange, String css) throws IOException {
        byte[] bytes = css.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/css; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void sendRedirect(HttpExchange exchange, String location) throws IOException {
        String safeLocation = location.replace("₹", "%E2%82%B9");
        exchange.getResponseHeaders().set("Location", safeLocation);
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }

    private void sendRedirectWithCookie(HttpExchange exchange, String location, String cookieValue) throws IOException {
        String safeLocation = location.replace("₹", "%E2%82%B9");
        exchange.getResponseHeaders().set("Set-Cookie", "session_id=" + cookieValue + "; Path=/; HttpOnly; SameSite=Lax");
        exchange.getResponseHeaders().set("Location", safeLocation);
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }
}
