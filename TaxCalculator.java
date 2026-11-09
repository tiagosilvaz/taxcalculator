package com.bartoszwalter.students.taxes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class TaxCalculator {
	private static final double PENSION_RATE_PERCENT = 9.76;
	private static final double DISABILITY_RATE_PERCENT = 1.5;
	private static final double SICKNESS_RATE_PERCENT = 2.45;
	private static final double HEALTH_CONTRIBUTION_FULL_RATE_PERCENT = 9;
	private static final double HEALTH_CONTRIBUTION_DEDUCTIBLE_RATE_PERCENT = 7.75;
	private static final double ADVANCE_TAX_RATE_PERCENT = 18;
	private static final double CIVIL_DEDUCTIBLE_EXPENSES_RATE_PERCENT = 20;
	private static final double EMPLOYMENT_DEDUCTIBLE_EXPENSES = 111.25;
	private static final double EMPLOYMENT_TAX_FREE_INCOME = 46.33;
	private static final DecimalFormat TWO_DECIMALS_FORMAT = new DecimalFormat("#.00");
	private static final DecimalFormat WHOLE_NUMBER_FORMAT = new DecimalFormat("#");

	private enum ContractType {
		EMPLOYMENT('E'),
		CIVIL('C'),
		UNKNOWN(' ');

		private final char code;

		ContractType(char code) {
			this.code = code;
		}

		public static ContractType fromCode(char code) {
			for (ContractType type : values()) {
				if (type.code == code) {
					return type;
				}
			}
			return UNKNOWN;
		}
	}

	public static void main(String[] args) {
		double income = 0;
		char contractCode = ' ';
		try {
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
			income = readIncome(inputReader);
			contractCode = readContractCode(inputReader);
		} catch (Exception ex) {
			System.out.println("Incorrect");
			System.err.println(ex);
			return;
		}

		switch (ContractType.fromCode(contractCode)) {
			case EMPLOYMENT:
				new EmploymentContract(income).printSummary();
				break;
			case CIVIL:
				new CivilContract(income).printSummary();
				break;
			default:
				System.out.println("Unknown type of contract!");
		}
	}

	private static double readIncome(BufferedReader inputReader) throws IOException {
		System.out.print("Enter income: ");
		return Double.parseDouble(inputReader.readLine());
	}

	private static char readContractCode(BufferedReader inputReader) throws IOException {
		System.out.print("Contract Type: (E)mployment, (C)ivil: ");
		return inputReader.readLine().charAt(0);
	}

	private static double percentOf(double value, double ratePercent) {
		return (value * ratePercent) / 100;
	}

	public static double calculateAdvanceTaxPaid(double advanceTax, double healthContributionDeductible, double taxFreeIncome) {
		return advanceTax - healthContributionDeductible - taxFreeIncome;
	}

	public static double calculateAdvanceTax(double taxedIncome) {
		return percentOf(taxedIncome, ADVANCE_TAX_RATE_PERCENT);
	}

	public static double calculatePensionContribution(double income) {
		return percentOf(income, PENSION_RATE_PERCENT);
	}

	public static double calculateDisabilityContribution(double income) {
		return percentOf(income, DISABILITY_RATE_PERCENT);
	}

	public static double calculateSicknessContribution(double income) {
		return percentOf(income, SICKNESS_RATE_PERCENT);
	}

	public static double calculateIncomeAfterSocialContributions(double income) {
		return income
				- calculatePensionContribution(income)
				- calculateDisabilityContribution(income)
				- calculateSicknessContribution(income);
	}

	public static double calculateSocialContributionsTotal(double income) {
		return calculatePensionContribution(income)
				+ calculateDisabilityContribution(income)
				+ calculateSicknessContribution(income);
	}

	public static double calculateHealthContributionFull(double income) {
		return percentOf(income, HEALTH_CONTRIBUTION_FULL_RATE_PERCENT);
	}

	public static double calculateHealthContributionDeductible(double income) {
		return percentOf(income, HEALTH_CONTRIBUTION_DEDUCTIBLE_RATE_PERCENT);
	}

	public static double calculateNetIncome(double incomeBase, double socialContributions, double healthContributionFull, double advanceTaxPaidRounded) {
		return incomeBase - (socialContributions + healthContributionFull + advanceTaxPaidRounded);
	}

	public static double roundToWholeNumber(double value) {
		return Double.parseDouble(WHOLE_NUMBER_FORMAT.format(value));
	}

	private static class EmploymentContract {
		private final double income;

		EmploymentContract(double income) {
			this.income = income;
		}

		private double taxedIncome() {
			return income - EMPLOYMENT_DEDUCTIBLE_EXPENSES;
		}

		private double taxedIncomeRounded() {
			return roundToWholeNumber(taxedIncome());
		}

		private double advanceTax() {
			return calculateAdvanceTax(taxedIncomeRounded());
		}

		private double taxPaid() {
			return advanceTax() - EMPLOYMENT_TAX_FREE_INCOME;
		}

		private double advanceTaxPaid() {
			return calculateAdvanceTaxPaid(advanceTax(), calculateHealthContributionDeductible(income), EMPLOYMENT_TAX_FREE_INCOME);
		}

		private double advanceTaxPaidRounded() {
			return roundToWholeNumber(advanceTaxPaid());
		}

		private double netIncome() {
			return calculateNetIncome(income, calculateSocialContributionsTotal(income), calculateHealthContributionFull(income), advanceTaxPaidRounded());
		}

		void printSummary() {
			System.out.println("EMPLOYMENT");
			System.out.println("Income " + income);
			System.out.println("Social security tax "
					+ TWO_DECIMALS_FORMAT.format(calculatePensionContribution(income)));
			System.out.println("Health social security tax    "
					+ TWO_DECIMALS_FORMAT.format(calculateDisabilityContribution(income)));
			System.out.println("Sickness social security tax  "
					+ TWO_DECIMALS_FORMAT.format(calculateSicknessContribution(income)));
			System.out.println("Income basis for health social security: "
					+ income);
			System.out.println("Health social security tax: 9% = "
					+ TWO_DECIMALS_FORMAT.format(calculateHealthContributionFull(income)) + " 7,75% = " + TWO_DECIMALS_FORMAT.format(calculateHealthContributionDeductible(income)));
			System.out.println("Tax deductible expenses "
					+ EMPLOYMENT_DEDUCTIBLE_EXPENSES);
			System.out.println("income " + taxedIncome()
					+ " rounded " + WHOLE_NUMBER_FORMAT.format(taxedIncomeRounded()));
			System.out.println("Advance tax 18 % = "
					+ advanceTax());
			System.out.println("Tax free income = " + EMPLOYMENT_TAX_FREE_INCOME);
			System.out.println("Reduced tax = "
					+ TWO_DECIMALS_FORMAT.format(taxPaid()));
			System.out.println("Advance tax paid = "
					+ TWO_DECIMALS_FORMAT.format(advanceTaxPaid()) + " rounded = "
					+ WHOLE_NUMBER_FORMAT.format(advanceTaxPaidRounded()));
			System.out.println();
			System.out.println("Net income = "
					+ TWO_DECIMALS_FORMAT.format(netIncome()));
		}
	}

	private static class CivilContract {
		private static final double TAX_FREE_INCOME = 0;
		private static final double TAX_ALREADY_PAID = 0;

		private final double income;

		CivilContract(double income) {
			this.income = income;
		}

		private double incomeAfterSocialContributions() {
			return calculateIncomeAfterSocialContributions(income);
		}

		private double healthContributionFull() {
			return calculateHealthContributionFull(incomeAfterSocialContributions());
		}

		private double healthContributionDeductible() {
			return calculateHealthContributionDeductible(incomeAfterSocialContributions());
		}

		private double taxDeductibleExpenses() {
			return percentOf(incomeAfterSocialContributions(), CIVIL_DEDUCTIBLE_EXPENSES_RATE_PERCENT);
		}

		private double taxedIncome() {
			return incomeAfterSocialContributions() - taxDeductibleExpenses();
		}

		private double taxedIncomeRounded() {
			return roundToWholeNumber(taxedIncome());
		}

		private double advanceTax() {
			return calculateAdvanceTax(taxedIncomeRounded());
		}

		private double advanceTaxPaid() {
			return calculateAdvanceTaxPaid(advanceTax(), healthContributionDeductible(), TAX_FREE_INCOME);
		}

		private double advanceTaxPaidRounded() {
			return roundToWholeNumber(advanceTaxPaid());
		}

		private double netIncome() {
			return calculateNetIncome(incomeAfterSocialContributions(), calculateSocialContributionsTotal(income), healthContributionFull(), advanceTaxPaidRounded());
		}

		void printSummary() {
			System.out.println("income " + income);
			System.out.println("Social security tax = "
					+ TWO_DECIMALS_FORMAT.format(calculatePensionContribution(income)));
			System.out.println("Health social security tax = "
					+ TWO_DECIMALS_FORMAT.format(calculateDisabilityContribution(income)));
			System.out.println("Sickness social security tax = "
					+ TWO_DECIMALS_FORMAT.format(calculateSicknessContribution(income)));
			System.out.println("Income for calculating health security tax: "
					+ incomeAfterSocialContributions());
			System.out.println("Health security tax: 9% = "
					+ TWO_DECIMALS_FORMAT.format(healthContributionFull()) + " 7,75% = " + TWO_DECIMALS_FORMAT.format(healthContributionDeductible()));
			System.out.println("Tax deductible expenses = "
					+ taxDeductibleExpenses());
			System.out.println("income to be taxed = " + taxedIncome()
					+ " rounded = " + WHOLE_NUMBER_FORMAT.format(taxedIncomeRounded()));
			System.out.println("Advance tax 18 % = "
					+ advanceTax());
			System.out.println("Already paid tax = "
					+ TWO_DECIMALS_FORMAT.format(TAX_ALREADY_PAID));
			System.out.println("Advance tax  = "
					+ TWO_DECIMALS_FORMAT.format(advanceTaxPaid()) + " rounded = "
					+ WHOLE_NUMBER_FORMAT.format(advanceTaxPaidRounded()));
			System.out.println();
			System.out.println("Net income = "
					+ TWO_DECIMALS_FORMAT.format(netIncome()));
		}
	}
}
