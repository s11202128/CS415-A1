import { useState } from "react";

export default function LoansTab({
  customers,
  customerMap,
  loanProducts,
  loanApplications,
  loanForm,
  setLoanForm,
  onSubmitLoan,
  loanMessage,
}) {
  const [activeView, setActiveView] = useState("apply");
  return (
    <section className="panel-grid">
      <article className="panel wide">
        <div style={{ display: "flex", gap: "10px", marginBottom: "20px", alignItems: "center" }}>
          <button
            type="button"
            onClick={() => setActiveView("products")}
            style={{
              padding: "10px 20px",
              backgroundColor: activeView === "products" ? "#0f6bcf" : "#e0e0e0",
              color: activeView === "products" ? "white" : "black",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
              fontWeight: "bold",
            }}
          >
            Loan Products
          </button>
          <button
            type="button"
            onClick={() => setActiveView("apply")}
            style={{
              padding: "10px 20px",
              backgroundColor: activeView === "apply" ? "#0f6bcf" : "#e0e0e0",
              color: activeView === "apply" ? "white" : "black",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
              fontWeight: "bold",
            }}
          >
            Apply For Loan
          </button>
          <button
            type="button"
            onClick={() => setActiveView("applications")}
            style={{
              padding: "10px 20px",
              backgroundColor: activeView === "applications" ? "#0f6bcf" : "#e0e0e0",
              color: activeView === "applications" ? "white" : "black",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
              fontWeight: "bold",
            }}
          >
            My Loan Applications
          </button>
        </div>

        {activeView === "products" ? (
          <>
            <h2>Loan Products (Website Advertisement)</h2>
            <table>
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Annual Rate</th>
                  <th>Max Amount</th>
                  <th>Term</th>
                </tr>
              </thead>
              <tbody>
                {loanProducts.map((lp) => (
                  <tr key={lp.id}>
                    <td>{lp.name}</td>
                    <td>{(lp.annualRate * 100).toFixed(2)}%</td>
                    <td>FJD {lp.maxAmount.toFixed(2)}</td>
                    <td>{lp.minTermMonths}-{lp.maxTermMonths} months</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </>
        ) : activeView === "apply" ? (
          <>
            <h2 className="loan-apply-title">Apply For Loan</h2>
            <form className="loan-form-horizontal" onSubmit={onSubmitLoan}>
              <label>
                Customer
                <select value={loanForm.customerId} onChange={(e) => setLoanForm({ ...loanForm, customerId: e.target.value })} required>
                  <option value="">Select</option>
                  {customers.map((c) => (
                    <option key={c.id} value={c.id}>{c.fullName}</option>
                  ))}
                </select>
              </label>
              <label>
                Loan Product
                <select
                  value={loanForm.loanProductId}
                  onChange={(e) => setLoanForm({ ...loanForm, loanProductId: e.target.value })}
                  required
                >
                  <option value="">Select</option>
                  {loanProducts.map((lp) => (
                    <option key={lp.id} value={lp.id}>{lp.name}</option>
                  ))}
                </select>
              </label>
              <label>
                Requested Amount
                <input
                  type="number"
                  min="1"
                  value={loanForm.requestedAmount}
                  onChange={(e) => setLoanForm({ ...loanForm, requestedAmount: e.target.value })}
                  required
                />
              </label>
              <label>
                Term (months)
                <input
                  type="number"
                  min="1"
                  value={loanForm.termMonths}
                  onChange={(e) => setLoanForm({ ...loanForm, termMonths: e.target.value })}
                  required
                />
              </label>
              <label>
                Purpose
                <input value={loanForm.purpose} onChange={(e) => setLoanForm({ ...loanForm, purpose: e.target.value })} required />
              </label>
              <label>
                Monthly Income
                <input
                  type="number"
                  min="0"
                  value={loanForm.monthlyIncome}
                  onChange={(e) => setLoanForm({ ...loanForm, monthlyIncome: e.target.value })}
                />
              </label>
              <label>
                Employment Status
                <input
                  value={loanForm.employmentStatus}
                  onChange={(e) => setLoanForm({ ...loanForm, employmentStatus: e.target.value })}
                />
              </label>
              <button type="submit">Submit Application</button>
            </form>
            <p className="status loan-apply-status">{loanMessage}</p>
          </>
        ) : (
          <>
            <h2>Submitted Loan Applications</h2>
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Customer</th>
                  <th>Product</th>
                  <th>Amount</th>
                  <th>Term</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {loanApplications.map((a, index) => (
                  <tr key={a.id}>
                    <td>{index + 1}</td>
                    <td>{customerMap[a.customerId]?.fullName || a.customerId}</td>
                    <td>{loanProducts.find((p) => p.id === a.loanProductId)?.name || a.loanProductId}</td>
                    <td>FJD {a.requestedAmount.toFixed(2)}</td>
                    <td>{a.termMonths}</td>
                    <td>{a.status}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </>
        )}
      </article>
    </section>
  );
}
