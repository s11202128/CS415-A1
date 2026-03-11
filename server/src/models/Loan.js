const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const Loan = sequelize.define('Loan', {
  id: {
    type: DataTypes.UUID,
    defaultValue: DataTypes.UUIDV4,
    primaryKey: true,
  },
  customerId: {
    type: DataTypes.UUID,
    allowNull: false,
  },
  loanType: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  principal: {
    type: DataTypes.DECIMAL(12, 2),
    allowNull: false,
  },
  interestRate: {
    type: DataTypes.DECIMAL(5, 2),
  },
  disbursedAmount: {
    type: DataTypes.DECIMAL(12, 2),
  },
  maturityDate: {
    type: DataTypes.DATE,
  },
  status: {
    type: DataTypes.STRING,
    defaultValue: 'active',
  },
}, {
  tableName: 'loans',
  timestamps: true,
});

module.exports = Loan;
