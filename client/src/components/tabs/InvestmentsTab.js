import { useState } from "react";

export default function InvestmentsTab({
  customers,
  customerMap,
  investments,
  investmentForm,
  setInvestmentForm,
  onAddInvestment,
  investmentMessage,
}) {
  const [activeView, setActiveView] = useState("create");
  return (
    <section className="panel-grid">
      <article className="panel wide">
        <div style={{ display: "flex", gap: "10px", marginBottom: "20px", alignItems: "center" }}>
          <button
            type="button"
            onClick={() => setActiveView("create")}
            style={{
              padding: "10px 20px",
              backgroundColor: activeView === "create" ? "#0f6bcf" : "#e0e0e0",
              color: activeView === "create" ? "white" : "black",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
              fontWeight: "bold",
            }}
          >
            Create Investment
          </button>
          <button
            type="button"
            onClick={() => setActiveView("list")}
            style={{
              padding: "10px 20px",
              backgroundColor: activeView === "list" ? "#0f6bcf" : "#e0e0e0",
              color: activeView === "list" ? "white" : "black",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
              fontWeight: "bold",
            }}
          >
            View Investments
          </button>
        </div>

        {activeView === "create" ? (
          <>
            <h2>Create Investment</h2>
            <form onSubmit={onAddInvestment}>
              <label>
                Customer
                <select
                  value={investmentForm.customerId}
                  onChange={(e) => setInvestmentForm({ ...investmentForm, customerId: e.target.value })}
                  required
                >
                  <option value="">Select</option>
                  {customers.map((c) => (
                    <option key={c.id} value={c.id}>{c.fullName}</option>
                  ))}
                </select>
              </label>
              <label>
                Product Name
                <input
                  value={investmentForm.name}
                  onChange={(e) => setInvestmentForm({ ...investmentForm, name: e.target.value })}
                  required
                />
              </label>
              <label>
                Amount
                <input
                  type="number"
                  min="1"
                  step="0.01"
                  value={investmentForm.amount}
                  onChange={(e) => setInvestmentForm({ ...investmentForm, amount: e.target.value })}
                  required
                />
              </label>
              <label>
                Annual Rate (decimal)
                <input
                  type="number"
                  step="0.0001"
                  value={investmentForm.annualRate}
                  onChange={(e) => setInvestmentForm({ ...investmentForm, annualRate: e.target.value })}
                  required
                />
              </label>
              <button type="submit">Create</button>
            </form>
            <p className="status">{investmentMessage}</p>
          </>
        ) : (
          <>
            <h2>Investments</h2>
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Customer</th>
                  <th>Name</th>
                  <th>Amount</th>
                  <th>Rate</th>
                </tr>
              </thead>
              <tbody>
                {investments.map((inv) => (
                  <tr key={inv.id}>
                    <td>{inv.id}</td>
                    <td>{customerMap[inv.customerId]?.fullName || inv.customerId}</td>
                    <td>{inv.name}</td>
                    <td>FJD {inv.amount.toFixed(2)}</td>
                    <td>{(inv.annualRate * 100).toFixed(2)}%</td>
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
