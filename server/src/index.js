const fs = require("fs");
const path = require("path");
const http = require("http");
const express = require("express");
const cors = require("cors");

function loadEnvFromFile() {
  const envPath = path.resolve(__dirname, "../.env");
  if (!fs.existsSync(envPath)) {
    return;
  }

  const content = fs.readFileSync(envPath, "utf8");
  content.split(/\r?\n/).forEach((line) => {
    const trimmed = line.trim();
    if (!trimmed || trimmed.startsWith("#")) {
      return;
    }
    const eqIndex = trimmed.indexOf("=");
    if (eqIndex === -1) {
      return;
    }
    const key = trimmed.slice(0, eqIndex).trim();
    const value = trimmed.slice(eqIndex + 1).trim();
    if (!process.env[key]) {
      process.env[key] = value;
    }
  });
}

loadEnvFromFile();

const authRoutes = require("./routes/authRoutes");
const apiRoutes = require("./routes/apiRoutes");
const initializeDatabase = require("./database");

const app = express();
const PORT = process.env.PORT || 4000;

app.use(cors());
app.use(express.json());

app.use("/api", authRoutes);
app.use("/api", apiRoutes);

app.use((err, req, res, next) => {
  console.error(err);
  res.status(500).json({ error: "Internal server error" });
});

// Initialize database and start server
initializeDatabase()
  .then(() => {
    const server = http.createServer(app);

    server.on("error", (error) => {
      if (error && error.code === "EADDRINUSE") {
        console.warn(
          `Port ${PORT} is already in use. Another backend instance is likely already running.`
        );
        console.warn("Keeping nodemon alive for file changes instead of crashing.");
        return;
      }

      console.error("Failed to start HTTP server:", error);
      process.exit(1);
    });

    server.listen(PORT, () => {
      console.log(`BoF Banking API running on http://localhost:${PORT}`);
    });
  })
  .catch((error) => {
    console.error("Failed to initialize database:", error);
    process.exit(1);
  });
