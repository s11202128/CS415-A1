const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const Transaction = sequelize.define('Transaction', {
  id: {
    type: DataTypes.BIGINT.UNSIGNED,
    autoIncrement: true,
    primaryKey: true,
  },
  accountId: {
    type: DataTypes.BIGINT.UNSIGNED,
    allowNull: false,
  },
  type: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  amount: {
    type: DataTypes.DECIMAL(12, 2),
    allowNull: false,
  },
  description: {
    type: DataTypes.STRING,
  },
  status: {
    type: DataTypes.STRING,
    defaultValue: 'completed',
  },
  balanceAfter: {
    type: DataTypes.DECIMAL(12, 2),
  },
}, {
  tableName: 'transactions',
  timestamps: true,
});

module.exports = Transaction;
