package com.bank.ui;

import com.bank.model.Account;
import com.bank.model.AuditLog;
import com.bank.model.Beneficiary;
import com.bank.model.Branch;
import com.bank.model.Card;
import com.bank.model.ChequeBookRequest;
import com.bank.model.Employee;
import com.bank.model.Feedback;
import com.bank.model.FixedDepositAccount;
import com.bank.model.Loan;
import com.bank.model.Notification;
import com.bank.model.RecurringDepositAccount;
import com.bank.model.SavingsGoal;
import com.bank.model.StandingInstruction;
import com.bank.model.SupportTicket;
import com.bank.model.Transaction;
import com.bank.model.UpiProfile;
import com.bank.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageBuilder {

    public static String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    public static String formatMoney(double amount) {
        return String.format("₹%.2f", amount);
    }

    private static String renderAlerts(String error, String success) {
        StringBuilder sb = new StringBuilder();
        if (error != null && !error.trim().isEmpty()) {
            sb.append("<div class=\"alert alert-danger\"><span>&#9888;</span> <div><strong>Notice:</strong> ").append(escapeHtml(error)).append("</div></div>");
        }
        if (success != null && !success.trim().isEmpty()) {
            sb.append("<div class=\"alert alert-success\"><span>&#10003;</span> <div><strong>Success:</strong> ").append(escapeHtml(success)).append("</div></div>");
        }
        return sb.toString();
    }

    private static String getInitials(String name) {
        if (name == null || name.isEmpty()) {
            return "U";
        }
        return name.substring(0, 1).toUpperCase();
    }

    private static String renderNavbar(User user) {
        return "";
    }

    private static String getQuickSwitchClass(String activeUser, String targetUser, String baseClass) {
        if (activeUser != null && activeUser.equalsIgnoreCase(targetUser)) {
            return baseClass + " active";
        }
        return baseClass;
    }

    private static String renderPageStart(String title, User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\">");
        sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        sb.append("<title>").append(escapeHtml(title)).append(" - Apex Banking</title>");
        sb.append("<link rel=\"stylesheet\" href=\"/style.css\">");
        sb.append("<script>");
        sb.append("function applyStoredTheme(){");
        sb.append("var theme = localStorage.getItem('apex_theme');");
        sb.append("if(!theme){theme = (document.cookie.match(/(^|; )theme=([^;]*)/)||[])[2];}");
        sb.append("if(theme === 'dark'){document.documentElement.classList.add('dark-mode');}");
        sb.append("else{document.documentElement.classList.remove('dark-mode');}");
        sb.append("}");
        sb.append("applyStoredTheme();");
        sb.append("function toggleTheme(){");
        sb.append("var isDark = document.documentElement.classList.toggle('dark-mode');");
        sb.append("var theme = 'light'; if(isDark){ theme = 'dark'; }");
        sb.append("localStorage.setItem('apex_theme', theme);");
        sb.append("document.cookie = 'theme=' + theme + '; Path=/; Max-Age=31536000; SameSite=Lax';");
        sb.append("}");
        sb.append("function switchSubTab(evt, targetId){");
        sb.append("var panes = document.querySelectorAll('.sub-tab-pane');");
        sb.append("for(var i=0; i<panes.length; i++){ panes[i].style.display = 'none'; }");
        sb.append("var btns = evt.currentTarget.parentElement.querySelectorAll('.sub-tab-btn');");
        sb.append("for(var j=0; j<btns.length; j++){ btns[j].classList.remove('active'); }");
        sb.append("var target = document.getElementById(targetId);");
        sb.append("if(target){ target.style.display = 'block'; }");
        sb.append("evt.currentTarget.classList.add('active');");
        sb.append("}");
        sb.append("</script>");
        sb.append("</head><body>");

        if (user == null) {
            sb.append("<div class=\"app-auth-layout\"><div class=\"auth-wrapper\">");
            sb.append("<div class=\"auth-header\"><a href=\"/login\" class=\"brand\"><span class=\"brand-badge\">⚡</span> <div><div class=\"brand-title\">Apex<span style=\"color:var(--primary);\">.</span></div></div></a><button type=\"button\" class=\"theme-toggle\" onclick=\"toggleTheme()\">&#9680; Theme</button></div>");
            sb.append("<main class=\"auth-main\">");
        } else {
            String active = "";
            if (title.contains("Dashboard")) {
                active = "dashboard";
            } else if (title.contains("Account") || title.contains("Deposit") || title.contains("FD") || title.contains("RD")) {
                active = "accounts";
            } else if (title.contains("Transfer")) {
                active = "transfers";
            } else if (title.contains("Card") || title.contains("Cheque")) {
                active = "cards";
            } else if (title.contains("Loan")) {
                active = "loans";
            } else if (title.contains("Statement")) {
                active = "statements";
            } else if (title.contains("Hub") || title.contains("Profile")) {
                active = "hub";
            } else if (title.contains("Staff") || title.contains("Admin")) {
                active = "admin";
            }

            sb.append("<div class=\"app-layout\">");
            sb.append("<aside class=\"sidebar\">");
            sb.append("<div class=\"sidebar-header\"><a href=\"/dashboard\" class=\"brand\"><span class=\"brand-badge\">⚡</span><div><div class=\"brand-title\">Apex<span style=\"color:var(--primary);\">.</span></div><div class=\"brand-tag\">NEOBANK ENTERPRISE</div></div></a></div>");
            sb.append("<nav class=\"sidebar-nav\">");
            sb.append("<div class=\"nav-section-title\">CORE BANKING</div>");

            sb.append("<a href=\"/dashboard\" class=\"nav-item");
            if (active.equals("dashboard")) {
                sb.append(" active");
            }
            sb.append("\"><span class=\"nav-icon\">&#128202;</span> Dashboard</a>");

            sb.append("<a href=\"/accounts\" class=\"nav-item");
            if (active.equals("accounts")) {
                sb.append(" active");
            }
            sb.append("\"><span class=\"nav-icon\">&#127974;</span> Accounts &amp; Vaults</a>");

            sb.append("<a href=\"/transfers\" class=\"nav-item");
            if (active.equals("transfers")) {
                sb.append(" active");
            }
            sb.append("\"><span class=\"nav-icon\">&#128184;</span> Transfers &amp; Pay</a>");

            sb.append("<a href=\"/cards\" class=\"nav-item");
            if (active.equals("cards")) {
                sb.append(" active");
            }
            sb.append("\"><span class=\"nav-icon\">&#128179;</span> Cards &amp; Limits</a>");

            sb.append("<a href=\"/loans\" class=\"nav-item");
            if (active.equals("loans")) {
                sb.append(" active");
            }
            sb.append("\"><span class=\"nav-icon\">&#128200;</span> Loans &amp; EMI</a>");

            sb.append("<div class=\"nav-section-title\">SERVICES &amp; LOGS</div>");

            sb.append("<a href=\"/statements\" class=\"nav-item");
            if (active.equals("statements")) {
                sb.append(" active");
            }
            sb.append("\"><span class=\"nav-icon\">&#128203;</span> Statements</a>");

            sb.append("<a href=\"/hub\" class=\"nav-item");
            if (active.equals("hub")) {
                sb.append(" active");
            }
            sb.append("\"><span class=\"nav-icon\">&#128100;</span> Customer Hub</a>");

            if (user.isStaff()) {
                sb.append("<div class=\"nav-section-title\">MANAGEMENT</div>");
                sb.append("<a href=\"/admin\" class=\"nav-item nav-item-admin");
                if (active.equals("admin")) {
                    sb.append(" active");
                }
                sb.append("\"><span class=\"nav-icon\">&#128737;</span> Staff Portal</a>");
            }

            sb.append("</nav>");

            sb.append("<div class=\"sidebar-footer\">");
            sb.append("<div class=\"sidebar-user-card\"><div class=\"user-avatar\">").append(getInitials(user.getUsername())).append("</div><div class=\"user-meta\"><div class=\"user-name\">").append(escapeHtml(user.getUsername())).append("</div><div class=\"user-badge\"><span class=\"status-dot\"></span> ").append(escapeHtml(user.getRole())).append("</div></div></div>");
            sb.append("<div class=\"sidebar-actions-row\"><button type=\"button\" class=\"theme-toggle\" onclick=\"toggleTheme()\">&#9680; Theme</button><a href=\"/logout\" class=\"btn btn-sm btn-outline\">Logout</a></div>");
            sb.append("</div>");
            sb.append("</aside>");

            sb.append("<div class=\"main-content-wrap\">");
            sb.append("<header class=\"app-topbar\">");
            sb.append("<div class=\"topbar-left\"><div class=\"topbar-breadcrumb\">Apex Banking &gt; ").append(escapeHtml(title)).append("</div></div>");
            sb.append("<div class=\"topbar-quickswitch\">");
            sb.append("<span class=\"quickswitch-tag\">⚡ Quick Switch:</span>");
            sb.append("<a href=\"/demo/switch?user=admin\" class=\"").append(getQuickSwitchClass(user.getUsername(), "admin", "qs-pill qs-admin")).append("\" title=\"Switch to Administrator\">👑 Admin</a>");
            sb.append("<a href=\"/demo/switch?user=manager\" class=\"").append(getQuickSwitchClass(user.getUsername(), "manager", "qs-pill qs-staff")).append("\" title=\"Switch to Branch Manager\">👔 Manager</a>");
            sb.append("<a href=\"/demo/switch?user=teller\" class=\"").append(getQuickSwitchClass(user.getUsername(), "teller", "qs-pill qs-staff")).append("\" title=\"Switch to Teller\">💼 Teller</a>");
            sb.append("<a href=\"/demo/switch?user=john\" class=\"").append(getQuickSwitchClass(user.getUsername(), "john", "qs-pill qs-cust")).append("\" title=\"Switch to Customer John (Savings)\">👤 John</a>");
            sb.append("<a href=\"/demo/switch?user=alice\" class=\"").append(getQuickSwitchClass(user.getUsername(), "alice", "qs-pill qs-cust")).append("\" title=\"Switch to Customer Alice (Current)\">👤 Alice</a>");
            sb.append("<a href=\"/demo/reset\" onclick=\"return confirm('Reset all accounts, loans, and balances to default demo state?');\" class=\"qs-pill qs-reset\" title=\"Reset Demo State\">🔄 Reset</a>");
            sb.append("</div>");
            sb.append("<div class=\"topbar-right\"><span class=\"badge badge-active\"><span class=\"status-dot\"></span> KYC: ").append(escapeHtml(user.getKycStatus())).append("</span></div>");
            sb.append("</header>");
            sb.append("<main class=\"page-content\">");
        }
        return sb.toString();
    }

    private static String renderPageEnd() {
        return "</main><footer class=\"footer\"><p>&copy; Apex Neobank Express &bull; Professional Core Architecture</p></footer></div></div></body></html>";
    }

    public static String getCss() {
        return ":root {"
                + "--primary: #059669;"
                + "--primary-light: #10b981;"
                + "--primary-dark: #047857;"
                + "--accent: #06b6d4;"
                + "--bg: #f8fafc;"
                + "--card-bg: #ffffff;"
                + "--text-main: #0f172a;"
                + "--text-muted: #64748b;"
                + "--border: #e2e8f0;"
                + "--input-bg: #ffffff;"
                + "--input-border: #cbd5e1;"
                + "--success-bg: #ecfdf5;"
                + "--success-text: #065f46;"
                + "--success-border: #a7f3d0;"
                + "--danger-bg: #fef2f2;"
                + "--danger-text: #991b1b;"
                + "--danger-border: #fecaca;"
                + "--card-shadow: 0 4px 20px -2px rgba(15, 23, 42, 0.05), 0 2px 6px -1px rgba(15, 23, 42, 0.03);"
                + "--card-shadow-hover: 0 16px 32px -4px rgba(16, 185, 129, 0.12), 0 4px 10px -2px rgba(15, 23, 42, 0.04);"
                + "--card-gradient-1: linear-gradient(135deg, #064e3b 0%, #065f46 50%, #0f172a 100%);"
                + "--card-gradient-2: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);"
                + "--font: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;"
                + "}"
                + "html.dark-mode {"
                + "--primary: #10b981;"
                + "--primary-light: #34d399;"
                + "--primary-dark: #059669;"
                + "--accent: #22d3ee;"
                + "--bg: #070b12;"
                + "--card-bg: #0e1422;"
                + "--text-main: #f8fafc;"
                + "--text-muted: #94a3b8;"
                + "--border: #1a2336;"
                + "--input-bg: #090d18;"
                + "--input-border: #243048;"
                + "--success-bg: rgba(16, 185, 129, 0.12);"
                + "--success-text: #34d399;"
                + "--success-border: rgba(16, 185, 129, 0.3);"
                + "--danger-bg: rgba(239, 68, 68, 0.12);"
                + "--danger-text: #f87171;"
                + "--danger-border: rgba(239, 68, 68, 0.3);"
                + "--card-shadow: 0 6px 24px -2px rgba(0, 0, 0, 0.6);"
                + "--card-shadow-hover: 0 20px 40px -4px rgba(16, 185, 129, 0.22);"
                + "--card-gradient-1: linear-gradient(135deg, #064e3b 0%, #047857 50%, #080c14 100%);"
                + "--card-gradient-2: linear-gradient(135deg, #1a2336 0%, #0e1422 100%);"
                + "}"
                + "* { box-sizing: border-box; margin: 0; padding: 0; transition: background-color 0.2s, border-color 0.2s, color 0.15s; }"
                + "body { font-family: var(--font); background-color: var(--bg); color: var(--text-main); min-height: 100vh; display: flex; flex-direction: column; -webkit-font-smoothing: antialiased; }"
                + ".app-layout { display: flex; min-height: 100vh; width: 100%; }"
                + ".sidebar { width: 270px; min-width: 270px; background: var(--card-bg); border-right: 1px solid var(--border); display: flex; flex-direction: column; justify-content: space-between; position: sticky; top: 0; height: 100vh; overflow-y: auto; z-index: 50; }"
                + ".sidebar-header { padding: 1.5rem 1.5rem 1.25rem 1.5rem; border-bottom: 1px solid var(--border); }"
                + ".brand { text-decoration: none; display: flex; align-items: center; gap: 0.75rem; }"
                + ".brand-badge { width: 36px; height: 36px; border-radius: 11px; background: linear-gradient(135deg, #10b981 0%, #06b6d4 100%); display: flex; align-items: center; justify-content: center; color: #022c22; font-size: 1.15rem; font-weight: 900; box-shadow: 0 4px 12px rgba(16, 185, 129, 0.35); flex-shrink: 0; }"
                + ".brand-title { font-size: 1.3rem; font-weight: 900; color: var(--text-main); letter-spacing: -0.03em; line-height: 1; }"
                + ".brand-tag { font-size: 0.65rem; font-weight: 800; color: var(--primary); letter-spacing: 0.08em; margin-top: 0.25rem; }"
                + ".sidebar-nav { padding: 1.25rem 1rem; display: flex; flex-direction: column; gap: 0.25rem; flex: 1; }"
                + ".nav-section-title { font-size: 0.68rem; font-weight: 800; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.08em; padding: 0.75rem 0.65rem 0.35rem 0.65rem; margin-top: 0.5rem; }"
                + ".nav-item { display: flex; align-items: center; gap: 0.75rem; padding: 0.65rem 0.85rem; border-radius: 12px; text-decoration: none; color: var(--text-muted); font-size: 0.875rem; font-weight: 600; border: 1px solid transparent; transition: all 0.15s ease; }"
                + ".nav-item:hover { color: var(--primary); background: rgba(16, 185, 129, 0.08); }"
                + ".nav-item.active { color: var(--primary); background: rgba(16, 185, 129, 0.14); font-weight: 700; border-color: rgba(16, 185, 129, 0.25); }"
                + ".nav-item-admin { color: #f59e0b; }"
                + ".nav-item-admin:hover { background: rgba(245, 158, 11, 0.1); color: #f59e0b; }"
                + ".nav-item-admin.active { background: rgba(245, 158, 11, 0.15); color: #f59e0b; border-color: rgba(245, 158, 11, 0.3); }"
                + ".sidebar-footer { padding: 1.25rem 1rem; border-top: 1px solid var(--border); background: var(--card-bg); }"
                + ".sidebar-user-card { display: flex; align-items: center; gap: 0.65rem; margin-bottom: 0.85rem; padding: 0.5rem 0.65rem; border-radius: 12px; background: rgba(16, 185, 129, 0.05); border: 1px solid var(--border); }"
                + ".user-avatar { width: 34px; height: 34px; border-radius: 10px; background: rgba(16, 185, 129, 0.15); border: 1px solid rgba(16, 185, 129, 0.3); color: var(--primary); display: flex; align-items: center; justify-content: center; font-weight: 800; font-size: 0.85rem; flex-shrink: 0; }"
                + ".user-meta { overflow: hidden; flex: 1; }"
                + ".user-name { font-size: 0.85rem; font-weight: 800; color: var(--text-main); white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }"
                + ".user-badge { font-size: 0.7rem; font-weight: 700; color: var(--text-muted); display: flex; align-items: center; gap: 0.35rem; }"
                + ".sidebar-actions-row { display: flex; gap: 0.5rem; justify-content: space-between; }"
                + ".main-content-wrap { flex: 1; min-width: 0; display: flex; flex-direction: column; }"
                + ".app-topbar { height: 60px; padding: 0 2rem; border-bottom: 1px solid var(--border); background: var(--card-bg); display: flex; align-items: center; justify-content: space-between; gap: 1rem; backdrop-filter: blur(12px); -webkit-backdrop-filter: blur(12px); position: sticky; top: 0; z-index: 40; }"
                + ".topbar-breadcrumb { font-size: 0.85rem; font-weight: 700; color: var(--text-muted); }"
                + ".topbar-quickswitch { display: flex; align-items: center; gap: 0.35rem; background: rgba(100, 116, 139, 0.08); padding: 0.2rem 0.5rem; border-radius: 9999px; border: 1px solid var(--border); overflow-x: auto; max-width: 60%; }"
                + ".quickswitch-tag { font-size: 0.72rem; font-weight: 800; color: var(--primary); margin-right: 0.2rem; white-space: nowrap; }"
                + ".qs-pill { font-size: 0.72rem; font-weight: 700; padding: 0.22rem 0.55rem; border-radius: 9999px; text-decoration: none; color: var(--text-muted); background: var(--card-bg); border: 1px solid var(--border); transition: all 0.15s ease; white-space: nowrap; display: inline-flex; align-items: center; gap: 0.2rem; }"
                + ".qs-pill:hover { color: var(--text-main); border-color: var(--primary-light); transform: translateY(-1px); }"
                + ".qs-pill.active { color: #ffffff; background: var(--primary); border-color: var(--primary); font-weight: 800; box-shadow: 0 2px 8px rgba(16, 185, 129, 0.35); }"
                + ".qs-reset { background: rgba(239, 68, 68, 0.08); border-color: rgba(239, 68, 68, 0.25); color: #ef4444; }"
                + ".qs-reset:hover { background: #ef4444; color: #ffffff; border-color: #ef4444; }"
                + ".demo-quick-box { background: rgba(16, 185, 129, 0.06); border: 1px solid rgba(16, 185, 129, 0.25); border-radius: 14px; padding: 1.1rem; margin-bottom: 1.25rem; }"
                + ".demo-quick-header { display: flex; align-items: center; gap: 0.4rem; color: var(--primary); font-size: 0.95rem; margin-bottom: 0.35rem; font-weight: 800; }"
                + ".demo-quick-buttons { display: grid; grid-template-columns: repeat(auto-fit, minmax(85px, 1fr)); gap: 0.5rem; }"
                + ".demo-btn { display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 0.55rem 0.3rem; background: var(--card-bg); border: 1px solid var(--border); border-radius: 10px; text-decoration: none; color: var(--text-main); text-align: center; transition: all 0.2s; box-shadow: 0 1px 3px rgba(0,0,0,0.05); }"
                + ".demo-btn:hover { transform: translateY(-2px); border-color: var(--primary-light); box-shadow: 0 6px 14px rgba(16, 185, 129, 0.18); }"
                + ".demo-badge { font-size: 0.65rem; font-weight: 800; padding: 0.1rem 0.35rem; border-radius: 4px; margin-bottom: 0.25rem; letter-spacing: 0.03em; }"
                + ".demo-btn-admin .demo-badge { background: #fef3c7; color: #92400e; }"
                + ".demo-btn-cust .demo-badge { background: #dbeafe; color: #1e40af; }"
                + ".demo-btn-staff .demo-badge { background: #ede9fe; color: #5b21b6; }"
                + ".demo-name { font-weight: 800; font-size: 0.85rem; line-height: 1.1; }"
                + ".demo-desc { font-size: 0.68rem; color: var(--text-muted); margin-top: 0.15rem; }"
                + ".page-content { max-width: 1240px; width: 100%; margin: 0 auto; padding: 2rem 2.5rem 3rem 2.5rem; flex: 1; }"
                + ".app-auth-layout { min-height: 100vh; display: flex; flex-direction: column; justify-content: center; padding: 2rem; background: var(--bg); }"
                + ".auth-wrapper { max-width: 520px; width: 100%; margin: 0 auto; }"
                + ".auth-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; }"
                + ".auth-main { width: 100%; }"
                + ".sub-tabs-bar { display: flex; gap: 0.5rem; overflow-x: auto; padding-bottom: 0.5rem; margin-bottom: 1.75rem; border-bottom: 1px solid var(--border); }"
                + ".sub-tab-btn { background: transparent; border: 1px solid transparent; color: var(--text-muted); font-weight: 700; font-size: 0.85rem; padding: 0.6rem 1.15rem; border-radius: 9999px; cursor: pointer; transition: all 0.2s ease; white-space: nowrap; font-family: inherit; }"
                + ".sub-tab-btn:hover { color: var(--text-main); background: rgba(16, 185, 129, 0.08); }"
                + ".sub-tab-btn.active { color: var(--primary); background: rgba(16, 185, 129, 0.15); border-color: rgba(16, 185, 129, 0.3); font-weight: 800; }"
                + ".sub-tab-pane { transition: opacity 0.2s; }"
                + ".hero-card { background: linear-gradient(135deg, rgba(16, 185, 129, 0.12) 0%, rgba(6, 182, 212, 0.08) 50%, var(--card-bg) 100%); border: 1px solid rgba(16, 185, 129, 0.25); border-radius: 24px; padding: 2.25rem; margin-bottom: 2rem; box-shadow: var(--card-shadow); }"
                + ".hero-top { display: flex; justify-content: space-between; align-items: flex-start; flex-wrap: wrap; gap: 1rem; margin-bottom: 1.5rem; }"
                + ".hero-label { font-size: 0.75rem; font-weight: 800; text-transform: uppercase; letter-spacing: 0.08em; color: var(--primary); }"
                + ".hero-balance { font-size: 3rem; font-weight: 900; color: var(--text-main); letter-spacing: -0.04em; line-height: 1.1; margin-top: 0.25rem; }"
                + ".hero-status { display: inline-flex; align-items: center; gap: 0.5rem; padding: 0.45rem 1rem; background: var(--card-bg); border: 1px solid var(--border); border-radius: 9999px; font-size: 0.8rem; font-weight: 700; color: var(--text-main); box-shadow: var(--card-shadow); }"
                + ".status-dot { width: 8px; height: 8px; border-radius: 9999px; background-color: #10b981; box-shadow: 0 0 8px #10b981; display: inline-block; }"
                + ".hero-actions { display: flex; gap: 0.75rem; flex-wrap: wrap; align-items: center; }"
                + ".hero-btn { display: inline-flex; align-items: center; gap: 0.45rem; padding: 0.65rem 1.25rem; border-radius: 9999px; font-size: 0.85rem; font-weight: 700; text-decoration: none; background: var(--card-bg); border: 1px solid var(--border); color: var(--text-main); transition: all 0.2s ease; }"
                + ".hero-btn:hover { transform: translateY(-2px); border-color: var(--primary); box-shadow: 0 6px 16px rgba(16, 185, 129, 0.15); }"
                + ".hero-btn-primary { background: var(--primary); border-color: var(--primary); color: #ffffff; box-shadow: 0 4px 14px rgba(16, 185, 129, 0.35); }"
                + ".hero-btn-primary:hover { background: var(--primary-dark); color: #ffffff; }"
                + ".page-title { font-size: 1.85rem; font-weight: 800; color: var(--text-main); margin-bottom: 0.25rem; letter-spacing: -0.03em; }"
                + ".page-subtitle { color: var(--text-muted); font-size: 0.925rem; margin-bottom: 1.75rem; }"
                + ".alert { padding: 0.95rem 1.25rem; border-radius: 14px; margin-bottom: 1.5rem; font-size: 0.925rem; display: flex; align-items: center; gap: 0.75rem; border: 1px solid transparent; }"
                + ".alert-danger { background-color: var(--danger-bg); color: var(--danger-text); border-color: var(--danger-border); }"
                + ".alert-success { background-color: var(--success-bg); color: var(--success-text); border-color: var(--success-border); }"
                + ".card { background: var(--card-bg); border-radius: 20px; border: 1px solid var(--border); box-shadow: var(--card-shadow); padding: 1.75rem; margin-bottom: 1.5rem; transition: transform 0.2s, box-shadow 0.2s; }"
                + ".card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.25rem; padding-bottom: 0.85rem; border-bottom: 1px solid var(--border); }"
                + ".card-title { font-size: 1.2rem; font-weight: 800; color: var(--text-main); letter-spacing: -0.01em; }"
                + ".grid-cards { display: grid; grid-template-columns: repeat(auto-fill, minmax(330px, 1fr)); gap: 1.35rem; margin-bottom: 1.5rem; }"
                + ".account-card { background: var(--card-bg); border: 1px solid var(--border); border-radius: 20px; padding: 1.6rem; box-shadow: var(--card-shadow); display: flex; flex-direction: column; justify-content: space-between; border-left: 6px solid #10b981; transition: transform 0.2s, box-shadow 0.2s; }"
                + ".account-card:hover { transform: translateY(-3px); box-shadow: var(--card-shadow-hover); }"
                + ".account-card.frozen { border-left-color: #ef4444; }"
                + ".account-card.fd { border-left-color: #8b5cf6; }"
                + ".account-card.rd { border-left-color: #f59e0b; }"
                + ".acc-top { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 0.75rem; }"
                + ".acc-num { font-size: 1.15rem; font-weight: 800; color: var(--primary); letter-spacing: 0.05em; font-family: monospace; }"
                + ".acc-holder { font-size: 0.85rem; color: var(--text-muted); margin-top: 0.2rem; }"
                + ".acc-balance-label { font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.06em; color: var(--text-muted); margin-bottom: 0.2rem; font-weight: 700; }"
                + ".acc-balance { font-size: 2.25rem; font-weight: 900; color: var(--text-main); margin-bottom: 0.75rem; letter-spacing: -0.03em; }"
                + ".acc-details { display: flex; flex-direction: column; gap: 0.45rem; font-size: 0.85rem; margin-bottom: 1.25rem; color: var(--text-muted); }"
                + ".acc-actions { display: flex; gap: 0.5rem; flex-wrap: wrap; margin-top: auto; padding-top: 1rem; border-top: 1px solid var(--border); }"
                + ".badge { display: inline-block; padding: 0.3rem 0.75rem; border-radius: 9999px; font-size: 0.725rem; font-weight: 800; text-transform: uppercase; letter-spacing: 0.05em; }"
                + ".badge-savings { background-color: rgba(16, 185, 129, 0.15); color: #10b981; border: 1px solid rgba(16, 185, 129, 0.25); }"
                + ".badge-current { background-color: rgba(6, 182, 212, 0.15); color: #06b6d4; border: 1px solid rgba(6, 182, 212, 0.25); }"
                + ".badge-fd { background-color: rgba(139, 92, 246, 0.15); color: #8b5cf6; border: 1px solid rgba(139, 92, 246, 0.25); }"
                + ".badge-rd { background-color: rgba(245, 158, 11, 0.15); color: #f59e0b; border: 1px solid rgba(245, 158, 11, 0.25); }"
                + ".badge-active { background-color: rgba(16, 185, 129, 0.15); color: #10b981; }"
                + ".badge-frozen { background-color: rgba(239, 68, 68, 0.15); color: #ef4444; }"
                + ".badge-admin { background-color: rgba(245, 158, 11, 0.15); color: #f59e0b; }"
                + ".badge-customer { background-color: rgba(100, 116, 139, 0.15); color: var(--text-muted); }"
                + ".table-container { overflow-x: auto; }"
                + "table { width: 100%; border-collapse: separate; border-spacing: 0; text-align: left; font-size: 0.9rem; }"
                + "th { background-color: rgba(100, 116, 139, 0.05); padding: 0.85rem 1.1rem; font-weight: 800; color: var(--text-muted); border-bottom: 1px solid var(--border); font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.06em; }"
                + "td { padding: 0.85rem 1.1rem; border-bottom: 1px solid var(--border); color: var(--text-main); vertical-align: middle; }"
                + "tr:hover td { background-color: rgba(16, 185, 129, 0.04); }"
                + ".amount-pos { color: #10b981; font-weight: 800; }"
                + ".amount-neg { color: #ef4444; font-weight: 800; }"
                + ".form-container { max-width: 600px; margin: 2rem auto; background: var(--card-bg); border-radius: 24px; border: 1px solid var(--border); box-shadow: var(--card-shadow-hover); padding: 2.5rem; }"
                + ".form-row { display: flex; gap: 1rem; margin-bottom: 1rem; }"
                + ".form-row .form-group { flex: 1; margin-bottom: 0; }"
                + ".form-group { margin-bottom: 1rem; }"
                + ".form-group label { display: block; font-size: 0.85rem; font-weight: 700; margin-bottom: 0.4rem; color: var(--text-main); }"
                + ".form-control { width: 100%; padding: 0.75rem 1rem; background-color: var(--input-bg); color: var(--text-main); border: 1px solid var(--input-border); border-radius: 12px; font-size: 0.925rem; font-family: inherit; transition: all 0.15s ease; }"
                + ".form-control:focus { outline: none; border-color: var(--primary); box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.2); }"
                + ".form-help { font-size: 0.775rem; color: var(--text-muted); margin-top: 0.35rem; }"
                + ".btn { display: inline-flex; align-items: center; justify-content: center; font-weight: 700; font-size: 0.875rem; padding: 0.65rem 1.35rem; border-radius: 12px; border: 1px solid transparent; cursor: pointer; text-decoration: none; transition: all 0.15s ease; }"
                + ".btn-primary { background-color: var(--primary); color: #ffffff; box-shadow: 0 4px 12px rgba(16, 185, 129, 0.25); }"
                + ".btn-primary:hover { background-color: var(--primary-light); transform: translateY(-1px); box-shadow: 0 6px 16px rgba(16, 185, 129, 0.35); }"
                + ".btn-secondary { background-color: var(--border); color: var(--text-main); }"
                + ".btn-secondary:hover { opacity: 0.85; transform: translateY(-1px); }"
                + ".btn-danger { background-color: #ef4444; color: #ffffff; }"
                + ".btn-danger:hover { background-color: #dc2626; transform: translateY(-1px); }"
                + ".btn-success { background-color: #10b981; color: #ffffff; }"
                + ".btn-success:hover { background-color: #059669; transform: translateY(-1px); }"
                + ".btn-outline { background-color: transparent; border-color: var(--border); color: var(--text-main); }"
                + ".btn-outline:hover { background-color: rgba(16, 185, 129, 0.08); border-color: var(--primary); color: var(--primary); }"
                + ".btn-sm { padding: 0.4rem 0.85rem; font-size: 0.8rem; border-radius: 10px; }"
                + ".btn-block { width: 100%; }"
                + ".stats-row { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1.25rem; margin-bottom: 2rem; }"
                + ".stat-card { background: var(--card-bg); border: 1px solid var(--border); border-radius: 18px; padding: 1.4rem; text-align: left; box-shadow: var(--card-shadow); transition: transform 0.2s; }"
                + ".stat-card:hover { transform: translateY(-2px); box-shadow: var(--card-shadow-hover); }"
                + ".stat-label { font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase; font-weight: 800; letter-spacing: 0.06em; }"
                + ".stat-value { font-size: 1.9rem; font-weight: 900; color: var(--text-main); margin-top: 0.35rem; letter-spacing: -0.02em; }"
                + ".stat-sub { font-size: 0.775rem; color: var(--text-muted); margin-top: 0.25rem; font-weight: 500; }"
                + ".tabs-nav { display: flex; gap: 0.5rem; border-bottom: 1px solid var(--border); margin-bottom: 1.75rem; overflow-x: auto; padding-bottom: 0.5rem; }"
                + ".tab-link { padding: 0.65rem 1.25rem; border-radius: 9999px; text-decoration: none; color: var(--text-muted); font-weight: 700; font-size: 0.875rem; border: 1px solid transparent; transition: all 0.15s ease; }"
                + ".tab-link:hover { color: var(--text-main); background-color: rgba(16, 185, 129, 0.08); }"
                + ".tab-link.active { background: rgba(16, 185, 129, 0.15); color: var(--primary); border-color: rgba(16, 185, 129, 0.3); }"
                + ".virtual-card { width: 100%; max-width: 380px; height: 220px; border-radius: 22px; padding: 1.6rem; color: #ffffff; display: flex; flex-direction: column; justify-content: space-between; box-shadow: 0 16px 36px -8px rgba(0, 0, 0, 0.45); border: 1px solid rgba(255, 255, 255, 0.15); position: relative; overflow: hidden; }"
                + ".card-theme-blue { background: var(--card-gradient-1); }"
                + ".card-theme-dark { background: var(--card-gradient-2); }"
                + ".chip-icon { width: 44px; height: 32px; background: linear-gradient(135deg, #fbbf24 0%, #d97706 100%); border-radius: 7px; display: inline-block; box-shadow: inset 0 1px 2px rgba(255,255,255,0.4); }"
                + ".card-display-num { font-size: 1.3rem; font-weight: 800; letter-spacing: 0.14em; font-family: monospace; text-shadow: 0 2px 4px rgba(0,0,0,0.5); margin: 0.75rem 0; }"
                + ".progress-bar { width: 100%; height: 10px; background-color: var(--border); border-radius: 9999px; overflow: hidden; margin-top: 0.5rem; }"
                + ".progress-fill { height: 100%; background: linear-gradient(90deg, #10b981, #06b6d4); border-radius: 9999px; }"
                + ".footer { text-align: center; padding: 2rem; color: var(--text-muted); font-size: 0.85rem; border-top: 1px solid var(--border); margin-top: auto; background-color: var(--card-bg); }"
                + ".pin-badge { display: inline-flex; align-items: center; gap: 0.25rem; font-size: 0.725rem; color: #f59e0b; background: rgba(245, 158, 11, 0.12); padding: 0.25rem 0.55rem; border-radius: 6px; font-weight: 800; }"
                + ".chart-box { padding: 1.75rem; background: var(--card-bg); border-radius: 20px; border: 1px solid var(--border); margin-bottom: 1.5rem; box-shadow: var(--card-shadow); }"
                + ".upi-box { text-align: center; padding: 2.25rem; background: var(--card-bg); border: 1px solid var(--border); border-radius: 20px; box-shadow: var(--card-shadow); }"
                + ".qr-mock { width: 140px; height: 140px; margin: 1rem auto; background: repeating-linear-gradient(0deg, #0f172a, #0f172a 10px, #ffffff 10px, #ffffff 20px); border: 6px solid #ffffff; border-radius: 12px; box-shadow: var(--card-shadow); }"
                + ".auth-switch { text-align: center; margin-top: 1.25rem; font-size: 0.875rem; color: var(--text-muted); }"
                + ".auth-switch a { color: var(--primary-light); text-decoration: none; font-weight: 700; }"
                + ".hint-box { margin-top: 1.5rem; padding: 1.1rem; background: rgba(16, 185, 129, 0.06); border: 1px dashed var(--primary-light); border-radius: 14px; font-size: 0.8rem; color: var(--text-muted); line-height: 1.5; }"
                + "@media (max-width: 900px) {"
                + ".app-layout { flex-direction: column; }"
                + ".sidebar { width: 100%; min-width: 100%; height: auto; position: static; border-right: none; border-bottom: 1px solid var(--border); }"
                + ".sidebar-nav { flex-direction: row; flex-wrap: wrap; }"
                + ".nav-section-title { display: none; }"
                + ".page-content { padding: 1.5rem 1rem; }"
                + "}";
    }

    public static String buildLoginPage(String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Sign In", null));
        sb.append("<div class=\"form-container\" style=\"max-width:520px;\">");
        sb.append("<h2 style=\"text-align:center; margin-bottom:0.5rem;\">Sign In to Apex</h2>");
        sb.append("<p style=\"text-align:center; color:var(--text-muted); font-size:0.9rem; margin-bottom:1.25rem;\">Enterprise Banking Portal</p>");
        sb.append(renderAlerts(error, success));

        sb.append("<div class=\"demo-quick-box\">");
        sb.append("<div class=\"demo-quick-header\"><span>⚡</span> <strong>1-Click Live Demo Sign-In</strong></div>");
        sb.append("<p style=\"font-size:0.8rem; color:var(--text-muted); margin-bottom:0.75rem;\">Click any role to log in instantly without typing passwords:</p>");
        sb.append("<div class=\"demo-quick-buttons\">");
        sb.append("<a href=\"/demo/switch?user=admin\" class=\"demo-btn demo-btn-admin\"><span class=\"demo-badge\">ADMIN</span><span class=\"demo-name\">admin</span><span class=\"demo-desc\">Staff Control</span></a>");
        sb.append("<a href=\"/demo/switch?user=john\" class=\"demo-btn demo-btn-cust\"><span class=\"demo-badge\">CUSTOMER</span><span class=\"demo-name\">john</span><span class=\"demo-desc\">Savings ₹1,500</span></a>");
        sb.append("<a href=\"/demo/switch?user=alice\" class=\"demo-btn demo-btn-cust\"><span class=\"demo-badge\">CUSTOMER</span><span class=\"demo-name\">alice</span><span class=\"demo-desc\">Current ₹2,500</span></a>");
        sb.append("<a href=\"/demo/switch?user=manager\" class=\"demo-btn demo-btn-staff\"><span class=\"demo-badge\">MANAGER</span><span class=\"demo-name\">manager</span><span class=\"demo-desc\">Loans &amp; KYC</span></a>");
        sb.append("<a href=\"/demo/switch?user=teller\" class=\"demo-btn demo-btn-staff\"><span class=\"demo-badge\">TELLER</span><span class=\"demo-name\">teller</span><span class=\"demo-desc\">Counter Ops</span></a>");
        sb.append("</div>");
        sb.append("</div>");

        sb.append("<div style=\"text-align:center; margin:1.25rem 0 1rem; font-size:0.8rem; color:var(--text-muted); text-transform:uppercase; letter-spacing:0.06em;\">&mdash; Or Sign In Manually &mdash;</div>");

        sb.append("<form method=\"POST\" action=\"/login\">");
        sb.append("<div class=\"form-group\"><label>Username</label><input type=\"text\" name=\"username\" class=\"form-control\" required autofocus></div>");
        sb.append("<div class=\"form-group\"><label>Password</label><input type=\"password\" name=\"password\" class=\"form-control\" required></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Sign In</button>");
        sb.append("</form>");
        sb.append("<div class=\"auth-switch\">Don't have an account? <a href=\"/register\">Register here</a> | <a href=\"/reset-password\">Forgot Password?</a></div>");
        sb.append("<div class=\"hint-box\"><strong>Test Roles (Default PIN: 1234):</strong><br>");
        sb.append("&bull; Admin: <code>admin</code> / <code>admin123</code><br>");
        sb.append("&bull; Manager: <code>manager</code> / <code>manager123</code><br>");
        sb.append("&bull; Teller: <code>teller</code> / <code>teller123</code><br>");
        sb.append("&bull; Customer: <code>john</code> / <code>password123</code></div>");
        sb.append("</div>");
        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildRegisterPage(String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Open Account", null));
        sb.append("<div class=\"form-container\">");
        sb.append("<h2 style=\"text-align:center; margin-bottom:0.5rem;\">New Customer Registration</h2>");
        sb.append("<p style=\"text-align:center; color:var(--text-muted); font-size:0.9rem; margin-bottom:1.5rem;\">Open a personal account with 2-step PIN security</p>");
        sb.append(renderAlerts(error, success));
        sb.append("<form method=\"POST\" action=\"/register\">");
        sb.append("<div class=\"form-group\"><label>Username</label><input type=\"text\" name=\"username\" class=\"form-control\" required autofocus></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Email</label><input type=\"email\" name=\"email\" class=\"form-control\" required></div>");
        sb.append("<div class=\"form-group\"><label>Phone</label><input type=\"tel\" name=\"phone\" class=\"form-control\" required></div></div>");
        sb.append("<div class=\"form-group\"><label>Residential Address</label><input type=\"text\" name=\"address\" class=\"form-control\" required></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Password</label><input type=\"password\" name=\"password\" class=\"form-control\" required></div>");
        sb.append("<div class=\"form-group\"><label>Confirm Password</label><input type=\"password\" name=\"confirmPassword\" class=\"form-control\" required></div></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>4-Digit PIN <span class=\"pin-badge\">2-Step</span></label><input type=\"password\" name=\"pin\" maxlength=\"4\" pattern=\"[0-9]{4}\" class=\"form-control\" required></div>");
        sb.append("<div class=\"form-group\"><label>Confirm PIN</label><input type=\"password\" name=\"confirmPin\" maxlength=\"4\" pattern=\"[0-9]{4}\" class=\"form-control\" required></div></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Submit Registration</button>");
        sb.append("</form>");
        sb.append("<div class=\"auth-switch\">Already registered? <a href=\"/login\">Sign in here</a></div>");
        sb.append("</div>");
        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildResetPasswordPage(String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Reset Password", null));
        sb.append("<div class=\"form-container\">");
        sb.append("<h2 style=\"text-align:center; margin-bottom:0.5rem;\">Password Recovery</h2>");
        sb.append("<p style=\"text-align:center; color:var(--text-muted); font-size:0.9rem; margin-bottom:1.5rem;\">Verify security answer to restore access and unlock account</p>");
        sb.append(renderAlerts(error, success));
        sb.append("<form method=\"POST\" action=\"/reset-password\">");
        sb.append("<div class=\"form-group\"><label>Username</label><input type=\"text\" name=\"username\" class=\"form-control\" required></div>");
        sb.append("<div class=\"form-group\"><label>Security Answer (Hint: default for test accounts is pet name / bank)</label><input type=\"text\" name=\"securityAnswer\" class=\"form-control\" required></div>");
        sb.append("<div class=\"form-group\"><label>New Password</label><input type=\"password\" name=\"newPassword\" class=\"form-control\" required></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Reset Password &amp; Unlock</button>");
        sb.append("</form>");
        sb.append("<div class=\"auth-switch\"><a href=\"/login\">Back to Sign In</a></div>");
        sb.append("</div>");
        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildDashboardPage(User user, List<Account> accounts, List<Transaction> recentTransactions, List<Card> cards, UpiProfile upi, List<Loan> loans, List<Notification> notifs, String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Dashboard", user));

        sb.append(renderAlerts(error, success));

        double netWorth = 0.0;
        for (Account a : accounts) {
            netWorth = netWorth + a.getBalance();
        }

        sb.append("<div class=\"hero-card\">");
        sb.append("<div class=\"hero-top\">");
        sb.append("<div><div class=\"hero-label\">TOTAL LIQUID CAPITAL</div><div class=\"hero-balance\">").append(formatMoney(netWorth)).append("</div></div>");
        sb.append("<div class=\"hero-status\"><span class=\"status-dot\"></span> KYC: ").append(escapeHtml(user.getKycStatus())).append("</div>");
        sb.append("</div>");
        sb.append("<div class=\"hero-actions\">");
        sb.append("<a href=\"/transfers\" class=\"hero-btn hero-btn-primary\"><span>&#8593;</span> Send Money</a>");
        sb.append("<a href=\"/deposit\" class=\"hero-btn\"><span>&#8595;</span> Add Deposit</a>");
        sb.append("<a href=\"/cards\" class=\"hero-btn\"><span>&#128179;</span> Cards &amp; Limits</a>");
        sb.append("<a href=\"/transfers\" class=\"hero-btn\"><span>&#9889;</span> Instant UPI</a>");
        sb.append("<a href=\"/accounts/new\" class=\"hero-btn\"><span>+</span> Open Account</a>");
        sb.append("<a href=\"/statements\" class=\"hero-btn\"><span>&#128203;</span> Statements</a>");
        sb.append("</div>");
        sb.append("</div>");

        sb.append("<div class=\"stats-row\">");
        sb.append("<div class=\"stat-card\"><div class=\"stat-label\">Accounts &amp; Deposits</div><div class=\"stat-value\">").append(accounts.size()).append("</div><div class=\"stat-sub\">Active Vaults</div></div>");
        sb.append("<div class=\"stat-card\"><div class=\"stat-label\">Savings Interest Rate</div><div class=\"stat-value\" style=\"color:#10b981;\">+4.0% p.a.</div><div class=\"stat-sub\">Automatic Monthly Credit</div></div>");
        sb.append("<div class=\"stat-card\"><div class=\"stat-label\">Active Credit &amp; Loans</div><div class=\"stat-value\">").append(loans.size()).append("</div><div class=\"stat-sub\">EMI Amortization</div></div>");
        sb.append("<div class=\"stat-card\"><div class=\"stat-label\">System Alerts</div><div class=\"stat-value\" style=\"color:#f59e0b;\">").append(notifs.size()).append("</div><div class=\"stat-sub\">2-Step PIN Protected</div></div>");
        sb.append("</div>");

        sb.append("<div class=\"grid-cards\">");
        for (Account a : accounts) {
            String cClass = "account-card";
            if (a.isFrozen()) {
                cClass = "account-card frozen";
            } else {
                if (a instanceof FixedDepositAccount) {
                    cClass = "account-card fd";
                } else {
                    if (a instanceof RecurringDepositAccount) {
                        cClass = "account-card rd";
                    }
                }
            }
            sb.append("<div class=\"").append(cClass).append("\">");
            sb.append("<div>");
            sb.append("<div class=\"acc-top\"><div><div class=\"acc-num\">").append(escapeHtml(a.getAccountNumber())).append("</div>");
            sb.append("<div class=\"acc-holder\">Branch: ").append(escapeHtml(a.getBranchCode())).append("</div></div>");
            sb.append("<span class=\"badge badge-savings\">").append(a.getAccountType()).append("</span></div>");

            sb.append("<div class=\"acc-balance-label\">Balance</div>");
            sb.append("<div class=\"acc-balance\">").append(formatMoney(a.getBalance())).append("</div>");

            sb.append("<div class=\"acc-details\">");
            sb.append("<div>Nominee: <strong>").append(escapeHtml(a.getNomineeName())).append(" (").append(escapeHtml(a.getNomineeRelation())).append(")</strong></div>");
            if (a.getOverdraftLimit() > 0) {
                sb.append("<div>Overdraft Facility: <strong style=\"color:#3b82f6;\">").append(formatMoney(a.getOverdraftLimit())).append("</strong></div>");
            }
            sb.append("<div>Monthly Interest: <strong style=\"color:#10b981;\">").append(formatMoney(a.calculateMonthlyInterest())).append("</strong></div>");
            sb.append("</div></div>");

            sb.append("<div class=\"acc-actions\">");
            sb.append("<a href=\"/deposit?account=").append(a.getAccountNumber()).append("\" class=\"btn btn-sm btn-secondary\">Deposit</a>");
            sb.append("<a href=\"/withdraw?account=").append(a.getAccountNumber()).append("\" class=\"btn btn-sm btn-secondary\">Withdraw</a>");
            sb.append("<a href=\"/transfers?source=").append(a.getAccountNumber()).append("\" class=\"btn btn-sm btn-primary\">Transfer</a>");
            sb.append("</div></div>");
        }
        sb.append("</div>");

        sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Recent Transactions</h2><a href=\"/statements\" class=\"btn btn-sm btn-outline\">Statements</a></div>");
        if (recentTransactions.isEmpty()) {
            sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem;\">No transactions on record.</p>");
        } else {
            sb.append("<div class=\"table-container\"><table><thead><tr><th>Txn ID</th><th>Type</th><th>Amount</th><th>Source</th><th>Target</th><th>Category</th><th>Timestamp</th></tr></thead><tbody>");
            int cnt = 0;
            for (Transaction t : recentTransactions) {
                if (cnt >= 5) {
                    break;
                }
                sb.append("<tr><td><code>").append(escapeHtml(t.getId())).append("</code></td>");
                sb.append("<td><span class=\"badge\">").append(escapeHtml(t.getType())).append("</span></td>");
                if (t.getType().equalsIgnoreCase("DEPOSIT") || t.getType().equalsIgnoreCase("INTEREST_CREDIT") || t.getType().equalsIgnoreCase("DISPUTE_REFUND")) {
                    sb.append("<td class=\"amount-pos\">+").append(formatMoney(t.getAmount())).append("</td>");
                } else {
                    sb.append("<td class=\"amount-neg\">-").append(formatMoney(t.getAmount())).append("</td>");
                }
                sb.append("<td>").append(escapeHtml(t.getSource())).append("</td>");
                sb.append("<td>").append(escapeHtml(t.getTarget())).append("</td>");
                sb.append("<td><span class=\"badge\">").append(escapeHtml(t.getCategory())).append("</span></td>");
                sb.append("<td>").append(escapeHtml(t.getTimestamp())).append("</td></tr>");
                cnt++;
            }
            sb.append("</tbody></table></div>");
        }
        sb.append("</div>");

        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildAccountsPage(User user, List<Account> accounts, List<Branch> branches, String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Accounts & Deposits", user));
        sb.append("<div style=\"display:flex; justify-content:space-between; align-items:flex-end; margin-bottom:1.5rem; flex-wrap:wrap; gap:1rem;\">");
        sb.append("<div><h1 class=\"page-title\">Accounts &amp; Deposits</h1><p class=\"page-subtitle\">Manage Savings, Current, Fixed Deposits, and Recurring Deposits</p></div>");
        sb.append("</div>");
        sb.append(renderAlerts(error, success));

        sb.append("<div class=\"sub-tabs-bar\">");
        sb.append("<button class=\"sub-tab-btn active\" onclick=\"switchSubTab(event, 'subtab-acc-list')\">📂 My Accounts (").append(accounts.size()).append(")</button>");
        sb.append("<button class=\"sub-tab-btn\" onclick=\"switchSubTab(event, 'subtab-acc-open')\">➕ Open Savings / Current</button>");
        sb.append("<button class=\"sub-tab-btn\" onclick=\"switchSubTab(event, 'subtab-acc-fd')\">📈 Book Fixed Deposit</button>");
        sb.append("<button class=\"sub-tab-btn\" onclick=\"switchSubTab(event, 'subtab-acc-rd')\">🔁 Start Recurring Deposit</button>");
        sb.append("</div>");

        sb.append("<div id=\"subtab-acc-list\" class=\"sub-tab-pane\">");
        if (accounts.isEmpty()) {
            sb.append("<div class=\"card\"><p style=\"color:var(--text-muted); font-size:0.9rem;\">No accounts found. Use the tabs above to open one.</p></div>");
        } else {
            sb.append("<div class=\"grid-cards\">");
            for (Account a : accounts) {
                sb.append("<div class=\"account-card\">");
                sb.append("<div class=\"acc-top\"><div><div class=\"acc-num\">").append(escapeHtml(a.getAccountNumber())).append("</div>");
                sb.append("<div class=\"acc-holder\">Branch: ").append(escapeHtml(a.getBranchCode())).append("</div></div>");
                sb.append("<span class=\"badge badge-savings\">").append(a.getAccountType()).append("</span></div>");

                sb.append("<div class=\"acc-balance-label\">Balance</div>");
                sb.append("<div class=\"acc-balance\">").append(formatMoney(a.getBalance())).append("</div>");

                sb.append("<div class=\"acc-details\">");
                sb.append("<div>Joint Holder: <strong>").append(escapeHtml(a.getJointHolderName())).append("</strong></div>");
                sb.append("<div>Nominee: <strong>").append(escapeHtml(a.getNomineeName())).append(" (").append(escapeHtml(a.getNomineeRelation())).append(")</strong></div>");
                if (a instanceof FixedDepositAccount) {
                    FixedDepositAccount fd = (FixedDepositAccount) a;
                    sb.append("<div>Term: <strong>").append(fd.getTermMonths()).append(" Months @ ").append(fd.getInterestRate() * 100).append("%</strong></div>");
                    sb.append("<div>Matures: <strong>").append(fd.getMaturityDate()).append(" (").append(formatMoney(fd.getMaturityAmount())).append(")</strong></div>");
                } else {
                    if (a instanceof RecurringDepositAccount) {
                        RecurringDepositAccount rd = (RecurringDepositAccount) a;
                        sb.append("<div>Monthly Installment: <strong>").append(formatMoney(rd.getMonthlyInstallment())).append(" (Paid: ").append(rd.getMonthsPaid()).append("/").append(rd.getTermMonths()).append(")</strong></div>");
                        sb.append("<div>Matures: <strong>").append(rd.getMaturityDate()).append(" (").append(formatMoney(rd.getMaturityAmount())).append(")</strong></div>");
                    } else {
                        sb.append("<div>Monthly Interest: <strong style=\"color:#10b981;\">").append(formatMoney(a.calculateMonthlyInterest())).append("</strong></div>");
                        if (a.getOverdraftLimit() > 0) {
                            sb.append("<div>Overdraft Limit: <strong style=\"color:#3b82f6;\">").append(formatMoney(a.getOverdraftLimit())).append("</strong></div>");
                        }
                    }
                }
                sb.append("</div>");
                sb.append("<div class=\"acc-actions\">");
                sb.append("<a href=\"/deposit?account=").append(a.getAccountNumber()).append("\" class=\"btn btn-sm btn-secondary\">Deposit</a>");
                sb.append("<a href=\"/withdraw?account=").append(a.getAccountNumber()).append("\" class=\"btn btn-sm btn-secondary\">Withdraw</a>");
                sb.append("<a href=\"/transfers?source=").append(a.getAccountNumber()).append("\" class=\"btn btn-sm btn-primary\">Transfer</a>");
                sb.append("</div></div>");
            }
            sb.append("</div>");
        }
        sb.append("</div>");

        sb.append("<div id=\"subtab-acc-open\" class=\"sub-tab-pane\" style=\"display:none;\">");
        sb.append("<div class=\"form-container\">");
        sb.append("<h2 style=\"margin-bottom:0.5rem;\">Open Savings or Current Account</h2>");
        sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem; margin-bottom:1.5rem;\">Includes joint holder options and nominee registration</p>");
        sb.append("<form method=\"POST\" action=\"/accounts/new\">");
        sb.append("<div class=\"form-group\"><label>Account Type</label><select name=\"accountType\" class=\"form-control\" required><option value=\"SAVINGS\">Savings Account (Min: ₹500.00, 4.0% Interest)</option><option value=\"CURRENT\">Current Account (Min: ₹0.00, ₹1,000 Overdraft)</option></select></div>");
        sb.append("<div class=\"form-group\"><label>Initial Deposit (₹)</label><input type=\"number\" step=\"0.01\" name=\"initialDeposit\" class=\"form-control\" placeholder=\"500.00\" required></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Joint Holder Username (Optional)</label><input type=\"text\" name=\"jointHolder\" class=\"form-control\" placeholder=\"Leave blank if none\"></div>");
        sb.append("<div class=\"form-group\"><label>Branch Code</label><select name=\"branchCode\" class=\"form-control\">");
        for (Branch b : branches) {
            sb.append("<option value=\"").append(escapeHtml(b.getCode())).append("\">").append(escapeHtml(b.getCode())).append(" - ").append(escapeHtml(b.getName())).append("</option>");
        }
        sb.append("</select></div></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Nominee Name</label><input type=\"text\" name=\"nomineeName\" class=\"form-control\" placeholder=\"e.g. Mary Doe\" required></div>");
        sb.append("<div class=\"form-group\"><label>Nominee Relationship</label><input type=\"text\" name=\"nomineeRelation\" class=\"form-control\" placeholder=\"e.g. Spouse / Sibling\" required></div></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Confirm &amp; Open Account</button>");
        sb.append("</form></div></div>");

        sb.append("<div id=\"subtab-acc-fd\" class=\"sub-tab-pane\" style=\"display:none;\">");
        sb.append("<div class=\"form-container\">");
        sb.append("<h2 style=\"margin-bottom:0.5rem;\">Book Fixed Deposit (FD)</h2>");
        sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem; margin-bottom:1.5rem;\">High yield guaranteed returns up to 8.0% p.a.</p>");
        sb.append("<form method=\"POST\" action=\"/accounts/fd\">");
        sb.append("<div class=\"form-group\"><label>Principal Deposit Amount (₹)</label><input type=\"number\" step=\"100\" min=\"1000\" name=\"principal\" class=\"form-control\" placeholder=\"Min ₹1,000.00\" required></div>");
        sb.append("<div class=\"form-group\"><label>Tenure (Months)</label><select name=\"termMonths\" class=\"form-control\"><option value=\"12\">12 Months (7.0% Interest)</option><option value=\"24\">24 Months (7.5% Interest)</option><option value=\"36\">36 Months (8.0% Interest)</option></select></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Nominee Name</label><input type=\"text\" name=\"nomineeName\" class=\"form-control\" required></div>");
        sb.append("<div class=\"form-group\"><label>Relationship</label><input type=\"text\" name=\"nomineeRelation\" class=\"form-control\" required></div></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Authorize &amp; Book FD</button>");
        sb.append("</form></div></div>");

        sb.append("<div id=\"subtab-acc-rd\" class=\"sub-tab-pane\" style=\"display:none;\">");
        sb.append("<div class=\"form-container\">");
        sb.append("<h2 style=\"margin-bottom:0.5rem;\">Recurring Deposit (RD)</h2>");
        sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem; margin-bottom:1.5rem;\">Discipline your savings monthly with 6.5% interest</p>");
        sb.append("<form method=\"POST\" action=\"/accounts/rd\">");
        sb.append("<div class=\"form-group\"><label>Monthly Installment (₹)</label><input type=\"number\" step=\"25\" min=\"100\" name=\"monthlyInstallment\" class=\"form-control\" placeholder=\"Min ₹100.00/mo\" required></div>");
        sb.append("<div class=\"form-group\"><label>Tenure (Months)</label><select name=\"termMonths\" class=\"form-control\"><option value=\"12\">12 Months</option><option value=\"24\">24 Months</option><option value=\"36\">36 Months</option></select></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Nominee Name</label><input type=\"text\" name=\"nomineeName\" class=\"form-control\" required></div>");
        sb.append("<div class=\"form-group\"><label>Relationship</label><input type=\"text\" name=\"nomineeRelation\" class=\"form-control\" required></div></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Start Recurring Deposit</button>");
        sb.append("</form></div></div>");

        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildOpenAccountPage(User user, List<Branch> branches, String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Open Account", user));
        sb.append("<div class=\"form-container\">");
        sb.append("<h2 style=\"margin-bottom:0.5rem;\">Open Savings or Current Account</h2>");
        sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem; margin-bottom:1.5rem;\">Includes joint holder options and nominee registration</p>");
        sb.append(renderAlerts(error, success));
        sb.append("<form method=\"POST\" action=\"/accounts/new\">");
        sb.append("<div class=\"form-group\"><label>Account Type</label><select name=\"accountType\" class=\"form-control\" required><option value=\"SAVINGS\">Savings Account (Min: ₹500.00, 4.0% Interest)</option><option value=\"CURRENT\">Current Account (Min: ₹0.00, ₹1,000 Overdraft)</option></select></div>");
        sb.append("<div class=\"form-group\"><label>Initial Deposit (₹)</label><input type=\"number\" step=\"0.01\" name=\"initialDeposit\" class=\"form-control\" placeholder=\"500.00\" required></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Joint Holder Username (Optional)</label><input type=\"text\" name=\"jointHolder\" class=\"form-control\" placeholder=\"Leave blank if none\"></div>");
        sb.append("<div class=\"form-group\"><label>Branch Code</label><select name=\"branchCode\" class=\"form-control\">");
        for (Branch b : branches) {
            sb.append("<option value=\"").append(escapeHtml(b.getCode())).append("\">").append(escapeHtml(b.getCode())).append(" - ").append(escapeHtml(b.getName())).append("</option>");
        }
        sb.append("</select></div></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Nominee Name</label><input type=\"text\" name=\"nomineeName\" class=\"form-control\" placeholder=\"e.g. Mary Doe\" required></div>");
        sb.append("<div class=\"form-group\"><label>Nominee Relationship</label><input type=\"text\" name=\"nomineeRelation\" class=\"form-control\" placeholder=\"e.g. Spouse / Sibling\" required></div></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Confirm &amp; Open Account</button>");
        sb.append("</form>");
        sb.append("</div>");
        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildFdPage(User user, List<Branch> branches, String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Book Fixed Deposit", user));
        sb.append("<div class=\"form-container\">");
        sb.append("<h2 style=\"margin-bottom:0.5rem;\">Book Fixed Deposit (FD)</h2>");
        sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem; margin-bottom:1.5rem;\">High yield guaranteed returns up to 8.0% p.a.</p>");
        sb.append(renderAlerts(error, success));
        sb.append("<form method=\"POST\" action=\"/accounts/fd\">");
        sb.append("<div class=\"form-group\"><label>Principal Deposit Amount (₹)</label><input type=\"number\" step=\"100\" min=\"1000\" name=\"principal\" class=\"form-control\" placeholder=\"Min ₹1,000.00\" required></div>");
        sb.append("<div class=\"form-group\"><label>Tenure (Months)</label><select name=\"termMonths\" class=\"form-control\"><option value=\"12\">12 Months (7.0% Interest)</option><option value=\"24\">24 Months (7.5% Interest)</option><option value=\"36\">36 Months (8.0% Interest)</option></select></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Nominee Name</label><input type=\"text\" name=\"nomineeName\" class=\"form-control\" required></div>");
        sb.append("<div class=\"form-group\"><label>Relationship</label><input type=\"text\" name=\"nomineeRelation\" class=\"form-control\" required></div></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Authorize &amp; Book FD</button>");
        sb.append("</form>");
        sb.append("</div>");
        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildRdPage(User user, List<Branch> branches, String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Start Recurring Deposit", user));
        sb.append("<div class=\"form-container\">");
        sb.append("<h2 style=\"margin-bottom:0.5rem;\">Recurring Deposit (RD)</h2>");
        sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem; margin-bottom:1.5rem;\">Discipline your savings monthly with 6.5% interest</p>");
        sb.append(renderAlerts(error, success));
        sb.append("<form method=\"POST\" action=\"/accounts/rd\">");
        sb.append("<div class=\"form-group\"><label>Monthly Installment (₹)</label><input type=\"number\" step=\"25\" min=\"100\" name=\"monthlyInstallment\" class=\"form-control\" placeholder=\"Min ₹100.00/mo\" required></div>");
        sb.append("<div class=\"form-group\"><label>Tenure (Months)</label><select name=\"termMonths\" class=\"form-control\"><option value=\"12\">12 Months</option><option value=\"24\">24 Months</option><option value=\"36\">36 Months</option></select></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Nominee Name</label><input type=\"text\" name=\"nomineeName\" class=\"form-control\" required></div>");
        sb.append("<div class=\"form-group\"><label>Relationship</label><input type=\"text\" name=\"nomineeRelation\" class=\"form-control\" required></div></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Start Recurring Deposit</button>");
        sb.append("</form>");
        sb.append("</div>");
        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildTransfersHubPage(User user, List<Account> accounts, List<Beneficiary> beneficiaries, List<StandingInstruction> siList, UpiProfile upi, String error, String success, String simulatedOtp) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Transfers &amp; Pay", user));
        sb.append("<h1 class=\"page-title\">Transfers &amp; Payments Hub</h1>");
        sb.append("<p class=\"page-subtitle\">Send funds, execute instant UPI, settle utility bills, schedule standing orders, or convert FX.</p>");
        sb.append(renderAlerts(error, success));

        if (simulatedOtp != null && !simulatedOtp.isEmpty()) {
            sb.append("<div class=\"alert alert-success\" style=\"border-left: 6px solid #f59e0b;\">");
            sb.append("<span>&#128274;</span> <strong>High-Value Transfer OTP:</strong> Enter this code on screen to authorize: <code style=\"font-size:1.2rem; font-weight:800; margin-left:0.5rem;\">").append(escapeHtml(simulatedOtp)).append("</code>");
            sb.append("</div>");
        }

        sb.append("<div class=\"sub-tabs-bar\">");
        sb.append("<button type=\"button\" class=\"sub-tab-btn active\" onclick=\"switchSubTab(event, 'subtab-send')\">&#9889; Direct Transfer</button>");
        sb.append("<button type=\"button\" class=\"sub-tab-btn\" onclick=\"switchSubTab(event, 'subtab-upi')\">&#128241; UPI Instant Pay</button>");
        sb.append("<button type=\"button\" class=\"sub-tab-btn\" onclick=\"switchSubTab(event, 'subtab-payees')\">&#128101; Saved Beneficiaries</button>");
        sb.append("<button type=\"button\" class=\"sub-tab-btn\" onclick=\"switchSubTab(event, 'subtab-bills')\">&#128161; Utility Bills</button>");
        sb.append("<button type=\"button\" class=\"sub-tab-btn\" onclick=\"switchSubTab(event, 'subtab-standing')\">&#128257; Standing Instructions</button>");
        sb.append("<button type=\"button\" class=\"sub-tab-btn\" onclick=\"switchSubTab(event, 'subtab-fx')\">&#128177; Currency FX</button>");
        sb.append("</div>");

        sb.append("<div id=\"subtab-send\" class=\"sub-tab-pane\">");
        sb.append("<div class=\"card\" style=\"max-width:680px; margin:0 auto;\">");
        sb.append("<div class=\"card-header\"><h2 class=\"card-title\">Direct Account Transfer</h2><span class=\"badge badge-active\">2-Step Verified</span></div>");
        sb.append("<form method=\"POST\" action=\"/transfers/send\">");
        sb.append("<div class=\"form-group\"><label>Source Account</label><select name=\"sourceAccount\" class=\"form-control\">");
        for (Account a : accounts) {
            if (!a.isFrozen()) {
                sb.append("<option value=\"").append(escapeHtml(a.getAccountNumber())).append("\">").append(escapeHtml(a.getAccountNumber())).append(" (").append(formatMoney(a.getBalance())).append(")</option>");
            }
        }
        sb.append("</select></div>");
        sb.append("<div class=\"form-group\"><label>Recipient Account Number</label><input type=\"text\" name=\"targetAccount\" class=\"form-control\" placeholder=\"e.g. ACC1002\" required></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Amount (₹)</label><input type=\"number\" step=\"0.01\" name=\"amount\" class=\"form-control\" placeholder=\"100.00\" required></div>");
        sb.append("<div class=\"form-group\"><label>Category</label><select name=\"category\" class=\"form-control\"><option value=\"TRANSFER\">Transfer</option><option value=\"BILLS\">Bills</option><option value=\"SHOPPING\">Shopping</option><option value=\"FOOD\">Food</option><option value=\"OTHER\">Other</option></select></div></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Security PIN <span class=\"pin-badge\">2-Step</span></label><input type=\"password\" name=\"pin\" maxlength=\"4\" class=\"form-control\" placeholder=\"****\" required></div>");
        sb.append("<div class=\"form-group\"><label>OTP (If &gt; ₹1,000)</label><input type=\"text\" name=\"otp\" class=\"form-control\" placeholder=\"6-digit OTP\"></div></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Authorize &amp; Transfer</button>");
        sb.append("</form>");
        sb.append("</div></div>");

        sb.append("<div id=\"subtab-upi\" class=\"sub-tab-pane\" style=\"display:none;\">");
        sb.append("<div style=\"display:grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: 1.5rem; max-width: 900px; margin: 0 auto;\">");
        sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Send via UPI</h2></div>");
        sb.append("<form method=\"POST\" action=\"/transfers/upi\">");
        sb.append("<div class=\"form-group\"><label>Recipient UPI ID (VPA)</label><input type=\"text\" name=\"targetUpi\" class=\"form-control\" placeholder=\"username@apex\" required></div>");
        sb.append("<div class=\"form-group\"><label>Amount (₹)</label><input type=\"number\" step=\"0.01\" name=\"amount\" class=\"form-control\" placeholder=\"50.00\" required></div>");
        sb.append("<div class=\"form-group\"><label>Security PIN <span class=\"pin-badge\">2-Step</span></label><input type=\"password\" name=\"pin\" maxlength=\"4\" class=\"form-control\" placeholder=\"****\" required></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Send UPI Payment</button>");
        sb.append("</form></div>");

        sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">My UPI Profile &amp; Limits</h2></div>");
        String vpa = user.getUsername() + "@apex";
        double uLimit = 2000.0;
        if (upi != null) {
            vpa = upi.getUpiId();
            uLimit = upi.getDailyLimit();
        }
        sb.append("<div style=\"text-align:center; margin-bottom:1rem;\">");
        sb.append("<div style=\"font-size:0.8rem; color:var(--text-muted); font-weight:700;\">YOUR VIRTUAL PAYMENT ADDRESS</div>");
        sb.append("<div style=\"font-size:1.25rem; font-weight:900; color:var(--primary); margin:0.35rem 0;\">").append(escapeHtml(vpa)).append("</div>");
        sb.append("<div class=\"qr-mock\"></div>");
        sb.append("</div>");
        sb.append("<form method=\"POST\" action=\"/upi/limit\">");
        sb.append("<input type=\"hidden\" name=\"upiId\" value=\"").append(escapeHtml(vpa)).append("\">");
        sb.append("<div class=\"form-group\"><label>Daily UPI Spending Limit (₹)</label><div style=\"display:flex; gap:0.5rem;\"><input type=\"number\" step=\"50\" name=\"dailyLimit\" value=\"").append((int) uLimit).append("\" class=\"form-control\" required><button type=\"submit\" class=\"btn btn-secondary btn-sm\">Set</button></div></div>");
        sb.append("</form></div>");
        sb.append("</div></div>");

        sb.append("<div id=\"subtab-payees\" class=\"sub-tab-pane\" style=\"display:none;\">");
        sb.append("<div class=\"card\" style=\"max-width:850px; margin:0 auto;\">");
        sb.append("<div class=\"card-header\"><h2 class=\"card-title\">Add Frequent Payee</h2></div>");
        sb.append("<form method=\"POST\" action=\"/beneficiaries/add\" style=\"margin-bottom:1.5rem;\">");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><input type=\"text\" name=\"payeeName\" class=\"form-control\" placeholder=\"Payee Full Name\" required></div>");
        sb.append("<div class=\"form-group\"><input type=\"text\" name=\"payeeAccount\" class=\"form-control\" placeholder=\"Account # (ACC...)\" required></div></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><input type=\"text\" name=\"nickname\" class=\"form-control\" placeholder=\"Nickname (e.g. Mom)\" required></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-secondary btn-sm\">+ Save Payee</button></div>");
        sb.append("</form>");
        if (beneficiaries.isEmpty()) {
            sb.append("<p style=\"color:var(--text-muted); font-size:0.85rem;\">No saved payees yet.</p>");
        } else {
            sb.append("<div class=\"table-container\"><table><thead><tr><th>Name</th><th>Account</th><th>Nickname</th><th>Action</th></tr></thead><tbody>");
            for (Beneficiary b : beneficiaries) {
                sb.append("<tr><td><strong>").append(escapeHtml(b.getPayeeName())).append("</strong></td><td><code>").append(escapeHtml(b.getPayeeAccountNumber())).append("</code></td><td>").append(escapeHtml(b.getNickname())).append("</td>");
                sb.append("<td><form method=\"POST\" action=\"/beneficiaries/remove\"><input type=\"hidden\" name=\"id\" value=\"").append(escapeHtml(b.getId())).append("\"><button type=\"submit\" class=\"btn btn-sm btn-danger\">Remove</button></form></td></tr>");
            }
            sb.append("</tbody></table></div>");
        }
        sb.append("</div></div>");

        sb.append("<div id=\"subtab-bills\" class=\"sub-tab-pane\" style=\"display:none;\">");
        sb.append("<div class=\"card\" style=\"max-width:680px; margin:0 auto;\">");
        sb.append("<div class=\"card-header\"><h2 class=\"card-title\">Utility &amp; Bill Payments (Simulated)</h2></div>");
        sb.append("<form method=\"POST\" action=\"/bills/pay\">");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Select Biller</label><select name=\"billerType\" class=\"form-control\"><option value=\"ELECTRICITY\">National Power &amp; Electric</option><option value=\"MOBILE_RECHARGE\">Apex Wireless 5G</option><option value=\"DTH\">Satellite DTH TV</option><option value=\"WATER\">Metro Municipal Water</option></select></div>");
        sb.append("<div class=\"form-group\"><label>Customer Reference / Mobile #</label><input type=\"text\" name=\"billerRef\" class=\"form-control\" placeholder=\"e.g. 9876543210\" required></div></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Bill Amount (₹)</label><input type=\"number\" step=\"0.01\" name=\"amount\" class=\"form-control\" placeholder=\"75.00\" required></div>");
        sb.append("<div class=\"form-group\"><label>Debit Account</label><select name=\"accountNumber\" class=\"form-control\">");
        for (Account a : accounts) {
            sb.append("<option value=\"").append(escapeHtml(a.getAccountNumber())).append("\">").append(escapeHtml(a.getAccountNumber())).append(" (").append(formatMoney(a.getBalance())).append(")</option>");
        }
        sb.append("</select></div></div>");
        sb.append("<div class=\"form-group\"><label>Security PIN <span class=\"pin-badge\">2-Step</span></label><input type=\"password\" name=\"pin\" maxlength=\"4\" class=\"form-control\" placeholder=\"****\" required></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-success btn-block\">Pay Utility Bill</button>");
        sb.append("</form>");
        sb.append("</div></div>");

        sb.append("<div id=\"subtab-standing\" class=\"sub-tab-pane\" style=\"display:none;\">");
        sb.append("<div class=\"card\" style=\"max-width:850px; margin:0 auto;\">");
        sb.append("<div class=\"card-header\"><h2 class=\"card-title\">Scheduled Standing Instructions</h2></div>");
        sb.append("<form method=\"POST\" action=\"/standing/new\" style=\"margin-bottom:1.25rem;\">");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>From Account</label><select name=\"source\" class=\"form-control\">");
        for (Account a : accounts) {
            sb.append("<option value=\"").append(escapeHtml(a.getAccountNumber())).append("\">").append(escapeHtml(a.getAccountNumber())).append("</option>");
        }
        sb.append("</select></div>");
        sb.append("<div class=\"form-group\"><label>To Account</label><input type=\"text\" name=\"target\" class=\"form-control\" placeholder=\"ACC...\" required></div>");
        sb.append("<div class=\"form-group\"><label>Amount (₹)</label><input type=\"number\" step=\"0.01\" name=\"amount\" class=\"form-control\" required></div>");
        sb.append("<div class=\"form-group\"><label>Frequency</label><select name=\"frequency\" class=\"form-control\"><option value=\"MONTHLY\">Monthly</option><option value=\"WEEKLY\">Weekly</option><option value=\"DAILY\">Daily</option></select></div>");
        sb.append("<div class=\"form-group\" style=\"align-self:flex-end;\"><button type=\"submit\" class=\"btn btn-primary\">+ Create Instruction</button></div></div>");
        sb.append("</form>");
        if (siList.isEmpty()) {
            sb.append("<p style=\"color:var(--text-muted); font-size:0.85rem;\">No automated recurring instructions set.</p>");
        } else {
            sb.append("<div class=\"table-container\"><table><thead><tr><th>ID</th><th>Source</th><th>Target</th><th>Amount</th><th>Frequency</th><th>Status</th><th>Action</th></tr></thead><tbody>");
            for (StandingInstruction s : siList) {
                sb.append("<tr><td><code>").append(escapeHtml(s.getId())).append("</code></td><td>").append(escapeHtml(s.getSourceAccount())).append("</td><td>").append(escapeHtml(s.getTargetAccount())).append("</td>");
                sb.append("<td>").append(formatMoney(s.getAmount())).append("</td><td>").append(escapeHtml(s.getFrequency())).append("</td><td><span class=\"badge badge-active\">").append(escapeHtml(s.getStatus())).append("</span></td>");
                sb.append("<td><form method=\"POST\" action=\"/standing/cancel\"><input type=\"hidden\" name=\"id\" value=\"").append(escapeHtml(s.getId())).append("\"><button type=\"submit\" class=\"btn btn-sm btn-outline\">Cancel</button></form></td></tr>");
            }
            sb.append("</tbody></table></div>");
        }
        sb.append("</div></div>");

        sb.append("<div id=\"subtab-fx\" class=\"sub-tab-pane\" style=\"display:none;\">");
        sb.append("<div class=\"card\" style=\"max-width:680px; margin:0 auto;\">");
        sb.append("<div class=\"card-header\"><h2 class=\"card-title\">Live Currency Converter</h2></div>");
        sb.append("<div style=\"font-size:0.875rem; color:var(--text-muted); margin-bottom:1rem;\">Reference rates against base 1.00 INR (₹): USD: 0.012, EUR: 0.011, GBP: 0.0095, AED: 0.044, JPY: 1.85, CAD: 0.016</div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Amount (INR / ₹)</label><input type=\"number\" id=\"convAmount\" value=\"1000\" class=\"form-control\" oninput=\"calcCurrency()\"></div>");
        sb.append("<div class=\"form-group\"><label>Target Currency</label><select id=\"convTarget\" class=\"form-control\" onchange=\"calcCurrency()\"><option value=\"0.012\">USD ($)</option><option value=\"0.011\">EUR (€)</option><option value=\"0.0095\">GBP (£)</option><option value=\"0.044\">AED (د.إ)</option><option value=\"1.85\">JPY (¥)</option><option value=\"0.016\">CAD (C$)</option></select></div></div>");
        sb.append("<div style=\"padding:1rem; background:rgba(16,185,129,0.1); border-radius:12px; font-weight:800; font-size:1.3rem; color:var(--primary); text-align:center; margin-top:1rem;\" id=\"convResult\">Converted: 12.00</div>");
        sb.append("<script>function calcCurrency(){var a=parseFloat(document.getElementById('convAmount').value)||0; var r=parseFloat(document.getElementById('convTarget').value)||0; document.getElementById('convResult').innerText='Converted: '+(a*r).toFixed(2);}</script>");
        sb.append("</div></div>");

        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildStatementsPage(User user, List<Transaction> transactions, String typeFilter, String catFilter, String start, String end, String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Statements &amp; Disputes", user));
        sb.append("<div style=\"display:flex; justify-content:space-between; align-items:flex-end; margin-bottom:1.5rem; flex-wrap:wrap; gap:1rem;\">");
        sb.append("<div><h1 class=\"page-title\">Account Statements</h1><p class=\"page-subtitle\">Mini-statement, date-range filtering, dispute filing, and statement exports.</p></div>");
        sb.append("<div style=\"display:flex; gap:0.5rem;\"><a href=\"/statement/download\" class=\"btn btn-primary btn-sm\">&#128190; Download CSV</a><button onclick=\"window.print()\" class=\"btn btn-secondary btn-sm\">&#128424; Print Statement</button></div>");
        sb.append("</div>");
        sb.append(renderAlerts(error, success));

        sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Filter Statement</h2></div>");
        sb.append("<form method=\"GET\" action=\"/statements\" class=\"form-row\">");
        sb.append("<div class=\"form-group\"><label>Type</label><select name=\"type\" class=\"form-control\"><option value=\"ALL\">All Types</option><option value=\"DEPOSIT\">Deposit</option><option value=\"WITHDRAWAL\">Withdrawal</option><option value=\"TRANSFER\">Transfer</option><option value=\"UPI_TRANSFER\">UPI</option><option value=\"BILL_PAY\">Bill Pay</option></select></div>");
        sb.append("<div class=\"form-group\"><label>Category</label><select name=\"category\" class=\"form-control\"><option value=\"ALL\">All Categories</option><option value=\"TRANSFER\">Transfer</option><option value=\"BILLS\">Bills</option><option value=\"SHOPPING\">Shopping</option><option value=\"FOOD\">Food</option><option value=\"INVESTMENT\">Investment</option></select></div>");
        sb.append("<div class=\"form-group\"><label>Start Date</label><input type=\"date\" name=\"start\" class=\"form-control\" value=\"").append(escapeHtml(start)).append("\"></div>");
        sb.append("<div class=\"form-group\"><label>End Date</label><input type=\"date\" name=\"end\" class=\"form-control\" value=\"").append(escapeHtml(end)).append("\"></div>");
        sb.append("<div class=\"form-group\" style=\"align-self:flex-end;\"><button type=\"submit\" class=\"btn btn-primary\">Filter</button></div>");
        sb.append("</form></div>");

        sb.append("<div class=\"card\">");
        if (transactions.isEmpty()) {
            sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem;\">No transactions match your query criteria.</p>");
        } else {
            sb.append("<div class=\"table-container\"><table><thead><tr><th>Txn ID</th><th>Type</th><th>Amount</th><th>Source</th><th>Target</th><th>Category</th><th>Timestamp</th><th>Dispute Action</th></tr></thead><tbody>");
            for (Transaction t : transactions) {
                sb.append("<tr><td><code>").append(escapeHtml(t.getId())).append("</code></td>");
                sb.append("<td><span class=\"badge\">").append(escapeHtml(t.getType())).append("</span></td>");
                if (t.getType().equalsIgnoreCase("DEPOSIT") || t.getType().equalsIgnoreCase("INTEREST_CREDIT") || t.getType().equalsIgnoreCase("DISPUTE_REFUND")) {
                    sb.append("<td class=\"amount-pos\">+").append(formatMoney(t.getAmount())).append("</td>");
                } else {
                    sb.append("<td class=\"amount-neg\">-").append(formatMoney(t.getAmount())).append("</td>");
                }
                sb.append("<td>").append(escapeHtml(t.getSource())).append("</td>");
                sb.append("<td>").append(escapeHtml(t.getTarget())).append("</td>");
                sb.append("<td><span class=\"badge\">").append(escapeHtml(t.getCategory())).append("</span></td>");
                sb.append("<td>").append(escapeHtml(t.getTimestamp())).append("</td>");
                sb.append("<td>");
                if (t.getDisputeStatus().equalsIgnoreCase("PENDING")) {
                    sb.append("<span class=\"badge badge-frozen\">DISPUTE PENDING</span>");
                } else {
                    if (t.getDisputeStatus().equalsIgnoreCase("APPROVED")) {
                        sb.append("<span class=\"badge badge-active\">REFUNDED</span>");
                    } else {
                        sb.append("<form method=\"POST\" action=\"/disputes/raise\" style=\"display:inline;\"><input type=\"hidden\" name=\"id\" value=\"").append(escapeHtml(t.getId())).append("\"><button type=\"submit\" class=\"btn btn-sm btn-outline\">Dispute</button></form>");
                    }
                }
                sb.append("</td></tr>");
            }
            sb.append("</tbody></table></div>");
        }
        sb.append("</div>");

        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildLoansPage(User user, List<Loan> loans, List<Account> accounts, String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Loans Portal", user));
        sb.append("<div style=\"display:flex; justify-content:space-between; align-items:flex-end; margin-bottom:1.5rem; flex-wrap:wrap; gap:1rem;\">");
        sb.append("<div><h1 class=\"page-title\">Loan Facility &amp; Repayment</h1><p class=\"page-subtitle\">Apply for Home, Car, or Personal Loans with instant monthly EMI amortization.</p></div>");
        sb.append("</div>");
        sb.append(renderAlerts(error, success));

        sb.append("<div class=\"sub-tabs-bar\">");
        sb.append("<button class=\"sub-tab-btn active\" onclick=\"switchSubTab(event, 'subtab-loans-active')\">📋 Active Loans (").append(loans.size()).append(")</button>");
        sb.append("<button class=\"sub-tab-btn\" onclick=\"switchSubTab(event, 'subtab-loans-apply')\">➕ Apply for New Loan</button>");
        sb.append("</div>");

        sb.append("<div id=\"subtab-loans-active\" class=\"sub-tab-pane\">");
        sb.append("<div class=\"card\">");
        sb.append("<div class=\"card-header\"><h2 class=\"card-title\">Active Loans &amp; EMI Repayment</h2></div>");
        if (loans.isEmpty()) {
            sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem;\">You have no active loans. Switch to 'Apply for New Loan' tab to submit an application.</p>");
        } else {
            for (Loan l : loans) {
                sb.append("<div style=\"border:1px solid var(--border); border-radius:10px; padding:1rem; margin-bottom:1rem;\">");
                sb.append("<div style=\"display:flex; justify-content:space-between; align-items:center; margin-bottom:0.5rem;\">");
                sb.append("<strong>").append(escapeHtml(l.getId())).append(" (").append(escapeHtml(l.getLoanType())).append(" LOAN)</strong>");
                sb.append("<span class=\"badge badge-active\">").append(escapeHtml(l.getStatus())).append("</span></div>");
                sb.append("<div style=\"font-size:0.85rem; color:var(--text-muted); margin-bottom:0.5rem;\">Remaining Balance: <strong style=\"color:#ef4444; font-size:1.1rem;\">").append(formatMoney(l.getRemainingBalance())).append("</strong> &bull; Monthly EMI: <strong>").append(formatMoney(l.getMonthlyEmi())).append("</strong></div>");
                if (l.isApproved() && l.getRemainingBalance() > 0.0) {
                    sb.append("<form method=\"POST\" action=\"/loans/repay\" class=\"form-row\" style=\"margin-bottom:0;\">");
                    sb.append("<input type=\"hidden\" name=\"loanId\" value=\"").append(escapeHtml(l.getId())).append("\">");
                    sb.append("<div class=\"form-group\"><select name=\"fromAccount\" class=\"form-control\">");
                    for (Account a : accounts) {
                        sb.append("<option value=\"").append(escapeHtml(a.getAccountNumber())).append("\">Pay from ").append(escapeHtml(a.getAccountNumber())).append("</option>");
                    }
                    sb.append("</select></div>");
                    sb.append("<div class=\"form-group\"><input type=\"password\" name=\"pin\" maxlength=\"4\" class=\"form-control\" placeholder=\"PIN\" required></div>");
                    sb.append("<button type=\"submit\" class=\"btn btn-sm btn-success\">Pay EMI</button>");
                    sb.append("</form>");
                }
                sb.append("</div>");
            }
        }
        sb.append("</div></div>");

        sb.append("<div id=\"subtab-loans-apply\" class=\"sub-tab-pane\" style=\"display:none;\">");
        sb.append("<div class=\"form-container\" style=\"max-width:700px;\">");
        sb.append("<h2 style=\"margin-bottom:0.5rem;\">Apply for New Loan</h2>");
        sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem; margin-bottom:1.5rem;\">Calculate your monthly EMI and apply instantly</p>");
        sb.append("<form method=\"POST\" action=\"/loans/apply\">");
        sb.append("<div class=\"form-group\"><label>Loan Category</label><select name=\"loanType\" class=\"form-control\" id=\"lType\" onchange=\"calcEmi()\"><option value=\"HOME\">Home Loan (8.5% p.a.)</option><option value=\"CAR\">Car Loan (9.5% p.a.)</option><option value=\"PERSONAL\">Personal Loan (12.5% p.a.)</option></select></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Principal (₹)</label><input type=\"number\" step=\"500\" min=\"1000\" name=\"principal\" id=\"lPrinc\" value=\"10000\" class=\"form-control\" oninput=\"calcEmi()\" required></div>");
        sb.append("<div class=\"form-group\"><label>Tenure (Months)</label><input type=\"number\" min=\"6\" max=\"240\" name=\"termMonths\" id=\"lMonths\" value=\"36\" class=\"form-control\" oninput=\"calcEmi()\" required></div></div>");
        sb.append("<div class=\"form-group\"><label>Disbursement Account</label><select name=\"disbursedAccount\" class=\"form-control\">");
        for (Account a : accounts) {
            sb.append("<option value=\"").append(escapeHtml(a.getAccountNumber())).append("\">").append(escapeHtml(a.getAccountNumber())).append("</option>");
        }
        sb.append("</select></div>");
        sb.append("<div style=\"padding:1rem; background:rgba(16,185,129,0.1); border:1px solid rgba(16,185,129,0.25); border-radius:12px; margin-bottom:1.5rem; font-weight:800; color:var(--primary); text-align:center; font-size:1.1rem;\" id=\"emiPreview\">Estimated EMI: ₹320.33 / month</div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Submit Loan Application</button>");
        sb.append("</form>");
        sb.append("<script>function calcEmi(){ var p=parseFloat(document.getElementById('lPrinc').value)||0; var m=parseInt(document.getElementById('lMonths').value)||1; var t=document.getElementById('lType').value; var r=0.085; if(t==='CAR'){r=0.095;}else if(t==='PERSONAL'){r=0.125;} var mr=r/12.0; var f=Math.pow(1.0+mr,m); var emi=(p*mr*f)/(f-1.0); document.getElementById('emiPreview').innerText='Estimated EMI: ₹'+emi.toFixed(2)+' / month'; } calcEmi();</script>");
        sb.append("</div></div>");

        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildHubPage(User user, List<SavingsGoal> goals, List<SupportTicket> tickets, List<Notification> notifs, Map<String, Double> categorySpending, String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("My Hub", user));
        sb.append("<h1 class=\"page-title\">Customer Hub &amp; Self-Service</h1>");
        sb.append("<p class=\"page-subtitle\">Manage profile credentials, KYC verification, savings targets, and support desk.</p>");
        sb.append(renderAlerts(error, success));

        sb.append("<div class=\"sub-tabs-bar\">");
        sb.append("<button class=\"sub-tab-btn active\" onclick=\"switchSubTab(event, 'subtab-hub-profile')\">👤 Profile &amp; KYC</button>");
        sb.append("<button class=\"sub-tab-btn\" onclick=\"switchSubTab(event, 'subtab-hub-security')\">🔒 Security &amp; Access</button>");
        sb.append("<button class=\"sub-tab-btn\" onclick=\"switchSubTab(event, 'subtab-hub-goals')\">🎯 Goals &amp; Spending</button>");
        sb.append("<button class=\"sub-tab-btn\" onclick=\"switchSubTab(event, 'subtab-hub-support')\">💬 Support &amp; Reviews</button>");
        sb.append("</div>");

        sb.append("<div id=\"subtab-hub-profile\" class=\"sub-tab-pane\">");
        sb.append("<div style=\"display:grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: 1.5rem;\">");
        sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Profile &amp; Contact Details</h2></div>");
        sb.append("<form method=\"POST\" action=\"/profile/update\">");
        sb.append("<div class=\"form-group\"><label>Email Address</label><input type=\"email\" name=\"email\" class=\"form-control\" value=\"").append(escapeHtml(user.getEmail())).append("\" required></div>");
        sb.append("<div class=\"form-group\"><label>Phone Number</label><input type=\"tel\" name=\"phone\" class=\"form-control\" value=\"").append(escapeHtml(user.getPhone())).append("\" required></div>");
        sb.append("<div class=\"form-group\"><label>Residential Address</label><input type=\"text\" name=\"address\" class=\"form-control\" value=\"").append(escapeHtml(user.getAddress())).append("\" required></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Save Profile Changes</button>");
        sb.append("</form></div>");

        sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">KYC Document Verification</h2><span class=\"badge badge-active\">").append(escapeHtml(user.getKycStatus())).append("</span></div>");
        sb.append("<p style=\"color:var(--text-muted); font-size:0.85rem; margin-bottom:1rem;\">Current on file: <strong>").append(escapeHtml(user.getKycDocument())).append("</strong></p>");
        sb.append("<form method=\"POST\" action=\"/kyc/submit\">");
        sb.append("<div class=\"form-group\"><label>Submit Identity Document</label><select name=\"documentName\" class=\"form-control\"><option value=\"National Passport\">National Passport</option><option value=\"Driver License\">Driver License</option><option value=\"Government National ID\">Government National ID</option><option value=\"Utility Proof of Residence\">Proof of Residence</option></select></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Submit for Staff Review</button>");
        sb.append("</form></div>");
        sb.append("</div></div>");

        sb.append("<div id=\"subtab-hub-security\" class=\"sub-tab-pane\" style=\"display:none;\">");
        sb.append("<div class=\"form-container\" style=\"max-width:550px;\">");
        sb.append("<h2 style=\"margin-bottom:0.5rem;\">Security &amp; Password</h2>");
        sb.append("<p style=\"color:var(--text-muted); font-size:0.85rem; margin-bottom:1.5rem;\">Update your secret credentials and protect your accounts.</p>");
        sb.append("<form method=\"POST\" action=\"/profile/password\">");
        sb.append("<div class=\"form-group\"><label>Current Password</label><input type=\"password\" name=\"oldPassword\" class=\"form-control\" required></div>");
        sb.append("<div class=\"form-group\"><label>New Password</label><input type=\"password\" name=\"newPassword\" class=\"form-control\" required></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-secondary btn-block\">Change Password</button>");
        sb.append("</form>");
        sb.append("<div class=\"hint-box\"><strong>Two-Step Security PIN:</strong> All financial operations (transfers, bill payments, withdrawals) are guarded by your personal 4-digit PIN configured during registration.</div>");
        sb.append("</div></div>");

        sb.append("<div id=\"subtab-hub-goals\" class=\"sub-tab-pane\" style=\"display:none;\">");
        sb.append("<div style=\"display:grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: 1.5rem;\">");
        sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Savings Goals Tracker</h2></div>");
        sb.append("<form method=\"POST\" action=\"/goals/new\" style=\"margin-bottom:1rem;\">");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><input type=\"text\" name=\"title\" class=\"form-control\" placeholder=\"e.g. Vacation Fund\" required></div>");
        sb.append("<div class=\"form-group\"><input type=\"number\" name=\"target\" class=\"form-control\" placeholder=\"Target (₹)\" required></div></div>");
        sb.append("<div class=\"form-row\"><div class=\"form-group\"><input type=\"date\" name=\"targetDate\" class=\"form-control\" required></div><button type=\"submit\" class=\"btn btn-primary btn-sm\">+ Add Goal</button></div>");
        sb.append("</form>");
        for (SavingsGoal g : goals) {
            sb.append("<div style=\"margin-bottom:1rem; border-top:1px solid var(--border); padding-top:0.75rem;\">");
            sb.append("<div style=\"display:flex; justify-content:space-between; font-size:0.9rem;\"><strong>").append(escapeHtml(g.getTitle())).append("</strong><span>").append(formatMoney(g.getCurrentAmount())).append(" / ").append(formatMoney(g.getTargetAmount())).append("</span></div>");
            sb.append("<div class=\"progress-bar\"><div class=\"progress-fill\" style=\"width:").append(String.format("%.1f", g.getProgressPercentage())).append("%;\"></div></div>");
            sb.append("</div>");
        }
        sb.append("</div>");

        sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Monthly Spending Breakdown</h2></div>");
        if (categorySpending.isEmpty()) {
            sb.append("<p style=\"color:var(--text-muted); font-size:0.85rem;\">No categorized expense activity this month.</p>");
        } else {
            for (Map.Entry<String, Double> entry : categorySpending.entrySet()) {
                sb.append("<div style=\"display:flex; justify-content:space-between; padding:0.5rem 0; border-bottom:1px solid var(--border); font-size:0.9rem;\">");
                sb.append("<span>&#9679; ").append(escapeHtml(entry.getKey())).append("</span><strong style=\"color:#ef4444;\">").append(formatMoney(entry.getValue())).append("</strong>");
                sb.append("</div>");
            }
        }
        sb.append("</div>");
        sb.append("</div></div>");

        sb.append("<div id=\"subtab-hub-support\" class=\"sub-tab-pane\" style=\"display:none;\">");
        sb.append("<div style=\"display:grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: 1.5rem;\">");
        sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Customer Support Helpdesk</h2></div>");
        sb.append("<form method=\"POST\" action=\"/tickets/new\" style=\"margin-bottom:1rem;\">");
        sb.append("<div class=\"form-group\"><input type=\"text\" name=\"subject\" class=\"form-control\" placeholder=\"Subject\" required></div>");
        sb.append("<div class=\"form-group\"><textarea name=\"description\" class=\"form-control\" placeholder=\"Describe your issue...\" required></textarea></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block btn-sm\">Raise Support Ticket</button>");
        sb.append("</form>");
        for (SupportTicket t : tickets) {
            sb.append("<div style=\"padding:0.75rem; border:1px solid var(--border); border-radius:8px; margin-bottom:0.75rem;\">");
            sb.append("<div style=\"display:flex; justify-content:space-between;\"><strong>").append(escapeHtml(t.getSubject())).append("</strong><span class=\"badge badge-savings\">").append(escapeHtml(t.getStatus())).append("</span></div>");
            sb.append("<div style=\"font-size:0.8rem; color:var(--text-muted); margin-top:0.25rem;\">Response: ").append(escapeHtml(t.getAdminReply())).append("</div>");
            sb.append("</div>");
        }
        sb.append("</div>");

        sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">App Feedback &amp; Review</h2></div>");
        sb.append("<form method=\"POST\" action=\"/feedback/submit\">");
        sb.append("<div class=\"form-group\"><label>Rating (1-5 Stars)</label><select name=\"rating\" class=\"form-control\"><option value=\"5\">5 Stars - Outstanding</option><option value=\"4\">4 Stars - Good</option><option value=\"3\">3 Stars - Average</option><option value=\"2\">2 Stars - Fair</option><option value=\"1\">1 Star - Needs Improvement</option></select></div>");
        sb.append("<div class=\"form-group\"><label>Comment</label><textarea name=\"comment\" class=\"form-control\" placeholder=\"Your thoughts on the system...\" required></textarea></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Submit Review</button>");
        sb.append("</form></div>");
        sb.append("</div></div>");

        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildAdminDashboard(User staff, List<User> users, List<Account> accounts, List<Transaction> txns, List<Loan> loans, List<Branch> branches, List<Employee> employees, List<AuditLog> auditLogs, List<Transaction> fraudAlerts, List<ChequeBookRequest> cheques, List<SupportTicket> tickets, String tab, String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Staff Administration Portal", staff));

        sb.append("<div style=\"display:flex; justify-content:space-between; align-items:flex-end; margin-bottom:1.5rem; flex-wrap:wrap; gap:1rem;\">");
        sb.append("<div><h1 class=\"page-title\">Staff Administration &amp; Operations</h1>");
        sb.append("<p class=\"page-subtitle\">Logged in as <strong>").append(escapeHtml(staff.getUsername())).append("</strong> (").append(escapeHtml(staff.getRole())).append(")</p></div>");
        sb.append("<form method=\"POST\" action=\"/admin/interest\"><button type=\"submit\" class=\"btn btn-success btn-sm\">&#9889; Run Monthly Savings Interest Batch</button></form>");
        sb.append("</div>");

        sb.append(renderAlerts(error, success));

        String tabOverview = "";
        if (tab.equals("overview")) {
            tabOverview = "active";
        }
        String tabUsers = "";
        if (tab.equals("users")) {
            tabUsers = "active";
        }
        String tabApprovals = "";
        if (tab.equals("approvals")) {
            tabApprovals = "active";
        }
        String tabLoans = "";
        if (tab.equals("loans")) {
            tabLoans = "active";
        }
        String tabBranches = "";
        if (tab.equals("branches")) {
            tabBranches = "active";
        }
        String tabAudit = "";
        if (tab.equals("audit")) {
            tabAudit = "active";
        }

        sb.append("<div class=\"tabs-nav\">");
        sb.append("<a href=\"/admin?tab=overview\" class=\"tab-link ").append(tabOverview).append("\">Overview &amp; Charts</a>");
        sb.append("<a href=\"/admin?tab=users\" class=\"tab-link ").append(tabUsers).append("\">Users &amp; Roles</a>");
        sb.append("<a href=\"/admin?tab=approvals\" class=\"tab-link ").append(tabApprovals).append("\">Approvals &amp; KYC</a>");
        sb.append("<a href=\"/admin?tab=loans\" class=\"tab-link ").append(tabLoans).append("\">Loans &amp; Disputes</a>");
        sb.append("<a href=\"/admin?tab=branches\" class=\"tab-link ").append(tabBranches).append("\">Branches &amp; Staff</a>");
        sb.append("<a href=\"/admin?tab=audit\" class=\"tab-link ").append(tabAudit).append("\">Security Audit Log</a>");
        sb.append("</div>");

        if (tab.equals("users")) {
            sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Register New User or Staff</h2></div>");
            sb.append("<form method=\"POST\" action=\"/admin/users/new\">");
            sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Username</label><input type=\"text\" name=\"username\" class=\"form-control\" required></div>");
            sb.append("<div class=\"form-group\"><label>Role</label><select name=\"role\" class=\"form-control\"><option value=\"CUSTOMER\">Customer</option><option value=\"ADMIN\">Administrator</option><option value=\"MANAGER\">Branch Manager</option><option value=\"TELLER\">Teller</option></select></div></div>");
            sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Email</label><input type=\"email\" name=\"email\" class=\"form-control\" required></div>");
            sb.append("<div class=\"form-group\"><label>Phone</label><input type=\"tel\" name=\"phone\" class=\"form-control\" required></div></div>");
            sb.append("<div class=\"form-group\"><label>Address</label><input type=\"text\" name=\"address\" class=\"form-control\" required></div>");
            sb.append("<div class=\"form-row\"><div class=\"form-group\"><label>Password</label><input type=\"password\" name=\"password\" class=\"form-control\" required></div>");
            sb.append("<div class=\"form-group\"><label>4-Digit PIN</label><input type=\"password\" name=\"pin\" maxlength=\"4\" class=\"form-control\" required></div></div>");
            sb.append("<button type=\"submit\" class=\"btn btn-primary\">Create Account</button>");
            sb.append("</form></div>");

            sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Registered User Directory</h2></div>");
            sb.append("<div class=\"table-container\"><table><thead><tr><th>Username</th><th>Role</th><th>Email</th><th>Phone</th><th>Locked?</th><th>Actions</th></tr></thead><tbody>");
            for (User u : users) {
                sb.append("<tr><td><strong>").append(escapeHtml(u.getUsername())).append("</strong></td><td><span class=\"badge badge-admin\">").append(escapeHtml(u.getRole())).append("</span></td>");
                sb.append("<td>").append(escapeHtml(u.getEmail())).append("</td><td>").append(escapeHtml(u.getPhone())).append("</td>");
                if (u.isLocked()) {
                    sb.append("<td><span class=\"badge badge-frozen\">LOCKED</span></td>");
                    sb.append("<td><div style=\"display:flex; gap:0.35rem; align-items:center;\"><form method=\"POST\" action=\"/admin/unlock\" style=\"display:inline;\"><input type=\"hidden\" name=\"username\" value=\"").append(escapeHtml(u.getUsername())).append("\"><button type=\"submit\" class=\"btn btn-sm btn-success\">Unlock</button></form>");
                } else {
                    sb.append("<td><span class=\"badge badge-active\">OK</span></td>");
                    sb.append("<td><div style=\"display:flex; gap:0.35rem; align-items:center;\"><form method=\"POST\" action=\"/admin/lock\" style=\"display:inline;\"><input type=\"hidden\" name=\"username\" value=\"").append(escapeHtml(u.getUsername())).append("\"><button type=\"submit\" class=\"btn btn-sm btn-secondary\">Lock</button></form>");
                }
                sb.append("<a href=\"/demo/switch?user=").append(escapeHtml(u.getUsername())).append("\" class=\"btn btn-sm btn-primary\" title=\"Impersonate / Switch user in 1 click\">⚡ Switch</a></div></td>");
                sb.append("</tr>");
            }
            sb.append("</tbody></table></div></div>");
        } else if (tab.equals("approvals")) {
            sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Account Applications Queue</h2></div>");
            boolean hasPending = false;
            for (Account a : accounts) {
                if (a.isPending()) {
                    hasPending = true;
                    sb.append("<div style=\"display:flex; justify-content:space-between; align-items:center; border-bottom:1px solid var(--border); padding:0.75rem 0;\">");
                    sb.append("<div><strong>").append(escapeHtml(a.getAccountNumber())).append("</strong> &bull; Holder: ").append(escapeHtml(a.getHolderName())).append(" &bull; Type: ").append(a.getAccountType()).append("</div>");
                    sb.append("<div style=\"display:flex; gap:0.5rem;\">");
                    sb.append("<form method=\"POST\" action=\"/admin/accounts/approve\"><input type=\"hidden\" name=\"accountNumber\" value=\"").append(escapeHtml(a.getAccountNumber())).append("\"><button type=\"submit\" class=\"btn btn-sm btn-success\">Approve</button></form>");
                    sb.append("<form method=\"POST\" action=\"/admin/accounts/reject\"><input type=\"hidden\" name=\"accountNumber\" value=\"").append(escapeHtml(a.getAccountNumber())).append("\"><button type=\"submit\" class=\"btn btn-sm btn-danger\">Reject</button></form>");
                    sb.append("</div></div>");
                }
            }
            if (!hasPending) {
                sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem;\">No accounts currently pending approval.</p>");
            }
            sb.append("</div>");

            sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">KYC Document Queue</h2></div>");
            boolean hasKyc = false;
            for (User u : users) {
                if (u.getKycStatus().equalsIgnoreCase("PENDING")) {
                    hasKyc = true;
                    sb.append("<div style=\"display:flex; justify-content:space-between; align-items:center; border-bottom:1px solid var(--border); padding:0.75rem 0;\">");
                    sb.append("<div><strong>").append(escapeHtml(u.getUsername())).append("</strong> submitted: <em>").append(escapeHtml(u.getKycDocument())).append("</em></div>");
                    sb.append("<div style=\"display:flex; gap:0.5rem;\">");
                    sb.append("<form method=\"POST\" action=\"/admin/kyc/review\"><input type=\"hidden\" name=\"username\" value=\"").append(escapeHtml(u.getUsername())).append("\"><input type=\"hidden\" name=\"approve\" value=\"true\"><button type=\"submit\" class=\"btn btn-sm btn-success\">Verify &amp; Approve</button></form>");
                    sb.append("<form method=\"POST\" action=\"/admin/kyc/review\"><input type=\"hidden\" name=\"username\" value=\"").append(escapeHtml(u.getUsername())).append("\"><input type=\"hidden\" name=\"approve\" value=\"false\"><button type=\"submit\" class=\"btn btn-sm btn-danger\">Reject</button></form>");
                    sb.append("</div></div>");
                }
            }
            if (!hasKyc) {
                sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem;\">No KYC documents pending review.</p>");
            }
            sb.append("</div>");
        } else if (tab.equals("loans")) {
            sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Pending Loan Approvals</h2></div>");
            boolean hasLoans = false;
            for (Loan l : loans) {
                if (l.isPending()) {
                    hasLoans = true;
                    sb.append("<div style=\"display:flex; justify-content:space-between; align-items:center; border-bottom:1px solid var(--border); padding:0.75rem 0;\">");
                    sb.append("<div><strong>").append(escapeHtml(l.getId())).append("</strong> &bull; User: ").append(escapeHtml(l.getUsername())).append(" &bull; Principal: ").append(formatMoney(l.getPrincipal())).append(" &bull; EMI: ").append(formatMoney(l.getMonthlyEmi())).append("</div>");
                    sb.append("<div style=\"display:flex; gap:0.5rem;\">");
                    sb.append("<form method=\"POST\" action=\"/admin/loans/review\"><input type=\"hidden\" name=\"loanId\" value=\"").append(escapeHtml(l.getId())).append("\"><input type=\"hidden\" name=\"approve\" value=\"true\"><button type=\"submit\" class=\"btn btn-sm btn-success\">Disburse Loan</button></form>");
                    sb.append("<form method=\"POST\" action=\"/admin/loans/review\"><input type=\"hidden\" name=\"loanId\" value=\"").append(escapeHtml(l.getId())).append("\"><input type=\"hidden\" name=\"approve\" value=\"false\"><button type=\"submit\" class=\"btn btn-sm btn-danger\">Decline</button></form>");
                    sb.append("</div></div>");
                }
            }
            if (!hasLoans) {
                sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem;\">No pending loan applications.</p>");
            }
            sb.append("</div>");

            sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Transaction Dispute Approvals</h2></div>");
            boolean hasDisputes = false;
            for (Transaction t : txns) {
                if (t.isDisputed()) {
                    hasDisputes = true;
                    sb.append("<div style=\"display:flex; justify-content:space-between; align-items:center; border-bottom:1px solid var(--border); padding:0.75rem 0;\">");
                    sb.append("<div><strong>").append(escapeHtml(t.getId())).append("</strong> &bull; Amount: ").append(formatMoney(t.getAmount())).append(" &bull; Source: ").append(escapeHtml(t.getSource())).append("</div>");
                    sb.append("<div style=\"display:flex; gap:0.5rem;\">");
                    sb.append("<form method=\"POST\" action=\"/admin/disputes/review\"><input type=\"hidden\" name=\"id\" value=\"").append(escapeHtml(t.getId())).append("\"><input type=\"hidden\" name=\"approve\" value=\"true\"><button type=\"submit\" class=\"btn btn-sm btn-success\">Approve Refund</button></form>");
                    sb.append("<form method=\"POST\" action=\"/admin/disputes/review\"><input type=\"hidden\" name=\"id\" value=\"").append(escapeHtml(t.getId())).append("\"><input type=\"hidden\" name=\"approve\" value=\"false\"><button type=\"submit\" class=\"btn btn-sm btn-danger\">Dismiss</button></form>");
                    sb.append("</div></div>");
                }
            }
            if (!hasDisputes) {
                sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem;\">No transaction disputes open.</p>");
            }
            sb.append("</div>");
        } else if (tab.equals("branches")) {
            sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Branches Directory</h2></div>");
            sb.append("<form method=\"POST\" action=\"/admin/branches/new\" class=\"form-row\" style=\"margin-bottom:1rem;\">");
            sb.append("<div class=\"form-group\"><input type=\"text\" name=\"code\" class=\"form-control\" placeholder=\"Branch Code (e.g. APEX0004)\" required></div>");
            sb.append("<div class=\"form-group\"><input type=\"text\" name=\"name\" class=\"form-control\" placeholder=\"Branch Name\" required></div>");
            sb.append("<div class=\"form-group\"><input type=\"text\" name=\"city\" class=\"form-control\" placeholder=\"City\" required></div>");
            sb.append("<div class=\"form-group\"><input type=\"text\" name=\"manager\" class=\"form-control\" placeholder=\"Manager Name\" required></div>");
            sb.append("<button type=\"submit\" class=\"btn btn-primary btn-sm\">+ Add Branch</button>");
            sb.append("</form>");
            sb.append("<div class=\"table-container\"><table><thead><tr><th>Code</th><th>Name</th><th>City</th><th>Manager</th></tr></thead><tbody>");
            for (Branch b : branches) {
                sb.append("<tr><td><strong>").append(escapeHtml(b.getCode())).append("</strong></td><td>").append(escapeHtml(b.getName())).append("</td><td>").append(escapeHtml(b.getCity())).append("</td><td>").append(escapeHtml(b.getManager())).append("</td></tr>");
            }
            sb.append("</tbody></table></div></div>");

            sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Employees &amp; Staff Salaries</h2></div>");
            sb.append("<div class=\"table-container\"><table><thead><tr><th>ID</th><th>Username</th><th>Name</th><th>Role</th><th>Dept</th><th>Salary</th><th>Hired</th></tr></thead><tbody>");
            for (Employee e : employees) {
                sb.append("<tr><td><code>").append(escapeHtml(e.getId())).append("</code></td><td>").append(escapeHtml(e.getUsername())).append("</td><td>").append(escapeHtml(e.getFullName())).append("</td><td>").append(escapeHtml(e.getRole())).append("</td><td>").append(escapeHtml(e.getDepartment())).append("</td><td>").append(formatMoney(e.getSalary())).append("</td><td>").append(escapeHtml(e.getJoinDate())).append("</td></tr>");
            }
            sb.append("</tbody></table></div></div>");
        } else if (tab.equals("audit")) {
            sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Enterprise Security Audit Log</h2></div>");
            sb.append("<div class=\"table-container\"><table><thead><tr><th>Audit ID</th><th>Actor</th><th>Action</th><th>Details</th><th>Timestamp</th></tr></thead><tbody>");
            for (AuditLog l : auditLogs) {
                sb.append("<tr><td><code>").append(escapeHtml(l.getId())).append("</code></td><td><strong>").append(escapeHtml(l.getActor())).append("</strong></td><td><span class=\"badge badge-admin\">").append(escapeHtml(l.getAction())).append("</span></td><td>").append(escapeHtml(l.getDetails())).append("</td><td>").append(escapeHtml(l.getTimestamp())).append("</td></tr>");
            }
            sb.append("</tbody></table></div></div>");
        } else {
            double totalDeposits = 0.0;
            for (Account a : accounts) {
                totalDeposits = totalDeposits + a.getBalance();
            }
            double totalLoansPrincipal = 0.0;
            for (Loan l : loans) {
                totalLoansPrincipal = totalLoansPrincipal + l.getPrincipal();
            }

            sb.append("<div class=\"stats-row\">");
            sb.append("<div class=\"stat-card\"><div class=\"stat-label\">Customers</div><div class=\"stat-value\">").append(users.size()).append("</div></div>");
            sb.append("<div class=\"stat-card\"><div class=\"stat-label\">Total Capital</div><div class=\"stat-value\" style=\"color:#10b981;\">").append(formatMoney(totalDeposits)).append("</div></div>");
            sb.append("<div class=\"stat-card\"><div class=\"stat-label\">Loans Issued</div><div class=\"stat-value\" style=\"color:#3b82f6;\">").append(formatMoney(totalLoansPrincipal)).append("</div></div>");
            sb.append("<div class=\"stat-card\"><div class=\"stat-label\">Audit Events</div><div class=\"stat-value\">").append(auditLogs.size()).append("</div></div>");
            sb.append("</div>");

            double depositTotal = 0.0;
            double withdrawTotal = 0.0;
            for (Transaction t : txns) {
                if (t.getType().equalsIgnoreCase("DEPOSIT") || t.getType().equalsIgnoreCase("INTEREST_CREDIT")) {
                    depositTotal = depositTotal + t.getAmount();
                } else if (t.getType().equalsIgnoreCase("WITHDRAWAL") || t.getType().equalsIgnoreCase("TRANSFER") || t.getType().equalsIgnoreCase("UPI_TRANSFER")) {
                    withdrawTotal = withdrawTotal + t.getAmount();
                }
            }
            double maxBar = depositTotal;
            if (withdrawTotal > depositTotal) {
                maxBar = withdrawTotal;
            }
            if (maxBar <= 0) {
                maxBar = 1000.0;
            }
            int depH = (int) ((depositTotal / maxBar) * 160.0);
            int withH = (int) ((withdrawTotal / maxBar) * 160.0);

            sb.append("<div class=\"chart-box\"><h2 class=\"card-title\" style=\"margin-bottom:1rem;\">Deposits vs. Withdrawals (Live Activity)</h2>");
            sb.append("<svg width=\"100%\" height=\"220\" viewBox=\"0 0 500 220\" style=\"background:rgba(100,116,139,0.04); border-radius:8px;\">");
            sb.append("<line x1=\"50\" y1=\"180\" x2=\"450\" y2=\"180\" stroke=\"#cbd5e1\" stroke-width=\"2\" />");
            sb.append("<rect x=\"120\" y=\"").append(180 - depH).append("\" width=\"80\" height=\"").append(depH).append("\" fill=\"#10b981\" rx=\"6\" />");
            sb.append("<text x=\"160\" y=\"").append(170 - depH).append("\" text-anchor=\"middle\" fill=\"#10b981\" font-weight=\"bold\" font-size=\"12\">").append(formatMoney(depositTotal)).append("</text>");
            sb.append("<text x=\"160\" y=\"200\" text-anchor=\"middle\" fill=\"#64748b\" font-size=\"13\" font-weight=\"bold\">Deposits</text>");
            sb.append("<rect x=\"300\" y=\"").append(180 - withH).append("\" width=\"80\" height=\"").append(withH).append("\" fill=\"#ef4444\" rx=\"6\" />");
            sb.append("<text x=\"340\" y=\"").append(170 - withH).append("\" text-anchor=\"middle\" fill=\"#ef4444\" font-weight=\"bold\" font-size=\"12\">").append(formatMoney(withdrawTotal)).append("</text>");
            sb.append("<text x=\"340\" y=\"200\" text-anchor=\"middle\" fill=\"#64748b\" font-size=\"13\" font-weight=\"bold\">Withdrawals</text>");
            sb.append("</svg></div>");

            if (!fraudAlerts.isEmpty()) {
                sb.append("<div class=\"card\" style=\"border-left:6px solid #ef4444;\"><div class=\"card-header\"><h2 class=\"card-title\" style=\"color:#ef4444;\">&#9888; Fraud Alert: High-Value Transactions (&gt; ₹5,000)</h2></div>");
                sb.append("<div class=\"table-container\"><table><thead><tr><th>Txn ID</th><th>Amount</th><th>Source</th><th>Target</th><th>Timestamp</th></tr></thead><tbody>");
                for (Transaction f : fraudAlerts) {
                    sb.append("<tr><td><code>").append(escapeHtml(f.getId())).append("</code></td><td style=\"color:#ef4444; font-weight:800;\">").append(formatMoney(f.getAmount())).append("</td><td>").append(escapeHtml(f.getSource())).append("</td><td>").append(escapeHtml(f.getTarget())).append("</td><td>").append(escapeHtml(f.getTimestamp())).append("</td></tr>");
                }
                sb.append("</tbody></table></div></div>");
            }
        }

        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildCardsPage(User user, List<Card> cards, List<ChequeBookRequest> cheques, List<Account> accounts, String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Cards &amp; Cheques", user));
        sb.append("<h1 class=\"page-title\">Cards &amp; Cheque Management</h1>");
        sb.append("<p class=\"page-subtitle\">Virtual debit cards, card daily spending limits, freeze locks, and chequebook requests.</p>");
        sb.append(renderAlerts(error, success));

        sb.append("<div class=\"sub-tabs-bar\">");
        sb.append("<button class=\"sub-tab-btn active\" onclick=\"switchSubTab(event, 'subtab-cards-list')\">💳 Virtual Cards (").append(cards.size()).append(")</button>");
        sb.append("<button class=\"sub-tab-btn\" onclick=\"switchSubTab(event, 'subtab-cheques-req')\">📖 Cheque Book Desk</button>");
        sb.append("</div>");

        sb.append("<div id=\"subtab-cards-list\" class=\"sub-tab-pane\">");
        if (cards.isEmpty()) {
            sb.append("<div class=\"card\"><p style=\"color:var(--text-muted); font-size:0.9rem;\">No virtual cards issued for this profile.</p></div>");
        } else {
            sb.append("<div style=\"display:grid; grid-template-columns: repeat(auto-fit, minmax(340px, 1fr)); gap: 1.5rem; margin-bottom:1.5rem;\">");
            for (Card c : cards) {
                sb.append("<div class=\"card\">");
                sb.append("<div class=\"virtual-card card-theme-blue\"><div class=\"card-chip-row\"><span class=\"chip-icon\"></span><span style=\"font-weight:900; font-style:italic;\">APEX VISA</span></div>");
                sb.append("<div class=\"card-display-num\">").append(escapeHtml(c.getCardNumber())).append("</div>");
                sb.append("<div style=\"display:flex; justify-content:space-between; font-size:0.8rem;\"><div><div>HOLDER</div><strong>").append(escapeHtml(c.getHolderName())).append("</strong></div><div><div>EXPIRES</div><strong>").append(escapeHtml(c.getExpiryDate())).append("</strong></div></div>");
                sb.append("</div>");

                sb.append("<div style=\"margin-top:1rem;\">");
                sb.append("<div style=\"display:flex; justify-content:space-between; align-items:center; margin-bottom:0.75rem;\"><span>Status:</span>");
                if (c.isFrozen()) {
                    sb.append("<span class=\"badge badge-frozen\">FROZEN</span>");
                } else {
                    sb.append("<span class=\"badge badge-active\">ACTIVE</span>");
                }
                sb.append("</div>");

                sb.append("<form method=\"POST\" action=\"/cards/limit\" style=\"margin-bottom:0.75rem;\">");
                sb.append("<input type=\"hidden\" name=\"cardNumber\" value=\"").append(escapeHtml(c.getCardNumber())).append("\">");
                sb.append("<div class=\"form-group\"><label>Daily Spending Limit (₹)</label><div style=\"display:flex; gap:0.5rem;\"><input type=\"number\" step=\"50\" name=\"dailyLimit\" value=\"").append((int) c.getDailyLimit()).append("\" class=\"form-control\" required><button type=\"submit\" class=\"btn btn-primary btn-sm\">Set</button></div></div>");
                sb.append("</form>");

                sb.append("<form method=\"POST\" action=\"/cards/toggle\">");
                sb.append("<input type=\"hidden\" name=\"cardNumber\" value=\"").append(escapeHtml(c.getCardNumber())).append("\">");
                if (c.isFrozen()) {
                    sb.append("<button type=\"submit\" class=\"btn btn-success btn-block btn-sm\">Unfreeze Card</button>");
                } else {
                    sb.append("<button type=\"submit\" class=\"btn btn-danger btn-block btn-sm\">Lock / Freeze Card</button>");
                }
                sb.append("</form>");
                sb.append("</div></div>");
            }
            sb.append("</div>");
        }
        sb.append("</div>");

        sb.append("<div id=\"subtab-cheques-req\" class=\"sub-tab-pane\" style=\"display:none;\">");
        sb.append("<div style=\"display:grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: 1.5rem;\">");
        sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Request New Cheque Book</h2></div>");
        sb.append("<form method=\"POST\" action=\"/cheques/request\">");
        sb.append("<div class=\"form-group\"><label>Select Account</label><select name=\"accountNumber\" class=\"form-control\">");
        for (Account a : accounts) {
            sb.append("<option value=\"").append(escapeHtml(a.getAccountNumber())).append("\">").append(escapeHtml(a.getAccountNumber())).append(" (").append(a.getAccountType()).append(")</option>");
        }
        sb.append("</select></div>");
        sb.append("<div class=\"form-group\"><label>Cheque Leaves</label><select name=\"leaves\" class=\"form-control\"><option value=\"25\">25 Leaves</option><option value=\"50\">50 Leaves</option><option value=\"100\">100 Leaves</option></select></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Request Cheque Book</button>");
        sb.append("</form></div>");

        sb.append("<div class=\"card\"><div class=\"card-header\"><h2 class=\"card-title\">Your Cheque Book Requests</h2></div>");
        if (cheques.isEmpty()) {
            sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem;\">No chequebook orders made yet.</p>");
        } else {
            sb.append("<div class=\"table-container\"><table><thead><tr><th>Req ID</th><th>Account</th><th>Leaves</th><th>Status</th></tr></thead><tbody>");
            for (ChequeBookRequest cq : cheques) {
                sb.append("<tr><td><code>").append(escapeHtml(cq.getId())).append("</code></td><td>").append(escapeHtml(cq.getAccountNumber())).append("</td><td>").append(cq.getLeavesCount()).append("</td><td><span class=\"badge badge-active\">").append(escapeHtml(cq.getStatus())).append("</span></td></tr>");
            }
            sb.append("</tbody></table></div>");
        }
        sb.append("</div></div></div>");

        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildDepositPage(User user, List<Account> accounts, String preselected, String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Deposit Funds", user));
        sb.append("<div class=\"form-container\"><h2 style=\"margin-bottom:0.5rem;\">Deposit Funds</h2>");
        sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem; margin-bottom:1.5rem;\">Credit money into your account with 2-step PIN authorization</p>");
        sb.append(renderAlerts(error, success));
        sb.append("<form method=\"POST\" action=\"/deposit\">");
        sb.append("<div class=\"form-group\"><label>Account</label><select name=\"accountNumber\" class=\"form-control\">");
        for (Account a : accounts) {
            if (!a.isFrozen()) {
                String sel = "";
                if (preselected != null && preselected.equalsIgnoreCase(a.getAccountNumber())) {
                    sel = " selected";
                }
                sb.append("<option value=\"").append(escapeHtml(a.getAccountNumber())).append("\"").append(sel).append(">").append(escapeHtml(a.getAccountNumber())).append(" (").append(formatMoney(a.getBalance())).append(")</option>");
            }
        }
        sb.append("</select></div>");
        sb.append("<div class=\"form-group\"><label>Amount (₹)</label><input type=\"number\" step=\"0.01\" name=\"amount\" class=\"form-control\" required></div>");
        sb.append("<div class=\"form-group\"><label>Security PIN <span class=\"pin-badge\">2-Step</span></label><input type=\"password\" name=\"pin\" maxlength=\"4\" class=\"form-control\" placeholder=\"****\" required></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-success btn-block\">Confirm Deposit</button>");
        sb.append("</form></div>");
        sb.append(renderPageEnd());
        return sb.toString();
    }

    public static String buildWithdrawPage(User user, List<Account> accounts, String preselected, String error, String success) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderPageStart("Withdraw Funds", user));
        sb.append("<div class=\"form-container\"><h2 style=\"margin-bottom:0.5rem;\">Withdraw Funds</h2>");
        sb.append("<p style=\"color:var(--text-muted); font-size:0.9rem; margin-bottom:1.5rem;\">Debit funds securely with 2-step PIN authorization</p>");
        sb.append(renderAlerts(error, success));
        sb.append("<form method=\"POST\" action=\"/withdraw\">");
        sb.append("<div class=\"form-group\"><label>Account</label><select name=\"accountNumber\" class=\"form-control\">");
        for (Account a : accounts) {
            if (!a.isFrozen()) {
                String sel = "";
                if (preselected != null && preselected.equalsIgnoreCase(a.getAccountNumber())) {
                    sel = " selected";
                }
                sb.append("<option value=\"").append(escapeHtml(a.getAccountNumber())).append("\"").append(sel).append(">").append(escapeHtml(a.getAccountNumber())).append(" (Balance: ").append(formatMoney(a.getBalance())).append(")</option>");
            }
        }
        sb.append("</select></div>");
        sb.append("<div class=\"form-group\"><label>Amount (₹)</label><input type=\"number\" step=\"0.01\" name=\"amount\" class=\"form-control\" required></div>");
        sb.append("<div class=\"form-group\"><label>Security PIN <span class=\"pin-badge\">2-Step</span></label><input type=\"password\" name=\"pin\" maxlength=\"4\" class=\"form-control\" placeholder=\"****\" required></div>");
        sb.append("<button type=\"submit\" class=\"btn btn-primary btn-block\">Confirm Withdrawal</button>");
        sb.append("</form></div>");
        sb.append(renderPageEnd());
        return sb.toString();
    }
}
