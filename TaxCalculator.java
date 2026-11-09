package com.bartoszwalter.students.taxes;

import java.io.BufferedReader;
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

	public static void main(String[] args) {
		double income = 0;
		char contractType = ' ';
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(System.in);
			BufferedReader inputReader = new BufferedReader(inputStreamReader);

			System.out.print("Enter income: ");
			income = Double.parseDouble(inputReader.readLine());

			System.out.print("Contract Type: (E)mployment, (C)ivil: ");
			contractType = inputReader.readLine().charAt(0);

		} catch (Exception ex) {
			System.out.println("Incorrect");
			System.err.println(ex);
			return;
		}

		DecimalFormat twoDecimalsFormat = new DecimalFormat("#.00");
		DecimalFormat wholeNumberFormat = new DecimalFormat("#");

		if (contractType == 'E') {
			System.out.println("EMPLOYMENT");
			System.out.println("Income " + income);
			double taxDeductibleExpenses = EMPLOYMENT_DEDUCTIBLE_EXPENSES;
			double taxFreeIncome = EMPLOYMENT_TAX_FREE_INCOME;
			double pensionContribution = calculatePensionContribution(income);
			double disabilityContribution = calculateDisabilityContribution(income);
			double sicknessContribution = calculateSicknessContribution(income);
			System.out.println("Social security tax "
					+ twoDecimalsFormat.format(pensionContribution));

			System.out.println("Health social security tax    "
					+ twoDecimalsFormat.format(disabilityContribution));
			System.out.println("Sickness social security tax  "
					+ twoDecimalsFormat.format(sicknessContribution));
			System.out
					.println("Income basis for health social security: "
							+ income);
			double healthContributionFull = calculateHealthContributionFull(income);
			double healthContributionDeductible = calculateHealthContributionDeductible(income);
			System.out.println("Health social security tax: 9% = "
					+ twoDecimalsFormat.format(healthContributionFull) + " 7,75% = " + twoDecimalsFormat.format(healthContributionDeductible));
			System.out.println("Tax deductible expenses "
					+ taxDeductibleExpenses
			);
			double taxedIncome = income - taxDeductibleExpenses ;
			double taxedIncome0 = Double
					.parseDouble(wholeNumberFormat.format(taxedIncome));
			System.out.println("income " + taxedIncome
					+ " rounded " + wholeNumberFormat.format(taxedIncome0));
			double advanceTax = calculateTax(taxedIncome0);
			System.out.println("Advance tax 18 % = "
					+ advanceTax);
			System.out.println("Tax free income = " + taxFreeIncome);
			double taxPaid = advanceTax - taxFreeIncome;
			System.out.println("Reduced tax = "
					+ twoDecimalsFormat.format(taxPaid));
			double advanceTaxPaid = calculateAdvanceTax(advanceTax, healthContributionDeductible, taxFreeIncome);
			double advanceTaxPaidRounded = Double.parseDouble(wholeNumberFormat.format(advanceTaxPaid));
			System.out.println("Advance tax paid = "
					+ twoDecimalsFormat.format(advanceTaxPaid) + " rounded = "
					+ wholeNumberFormat.format(advanceTaxPaidRounded));
			double netIncome = income
					- ((pensionContribution + disabilityContribution + sicknessContribution) + healthContributionFull + advanceTaxPaidRounded);
			System.out.println();
			System.out
					.println("Net income = "
							+ twoDecimalsFormat.format(netIncome));
		} else if (contractType == 'C') {
			System.out.println("income " + income);
			double pensionContribution = calculatePensionContribution(income);
			double disabilityContribution = calculateDisabilityContribution(income);
			double sicknessContribution = calculateSicknessContribution(income);
			double incomeAfterSocialContributions = calculateIncomeAfterSocialContributions(income);
			System.out.println("Social security tax = "
					+ twoDecimalsFormat.format(pensionContribution));
			System.out.println("Health social security tax = "
					+ twoDecimalsFormat.format(disabilityContribution));
			System.out.println("Sickness social security tax = "
					+ twoDecimalsFormat.format(sicknessContribution));
			System.out
					.println("Income for calculating health security tax: "
							+ incomeAfterSocialContributions);
			double healthContributionFull = calculateHealthContributionFull(incomeAfterSocialContributions);
			double healthContributionDeductible = calculateHealthContributionDeductible(incomeAfterSocialContributions);
			System.out.println("Health security tax: 9% = "
					+ twoDecimalsFormat.format(healthContributionFull) + " 7,75% = " + twoDecimalsFormat.format(healthContributionDeductible));
			double taxFreeIncome = 0;
			double taxDeductibleExpenses  = (incomeAfterSocialContributions * CIVIL_DEDUCTIBLE_EXPENSES_RATE_PERCENT) / 100;
			System.out.println("Tax deductible expenses = "
					+ taxDeductibleExpenses
			);
			double taxedIncome = incomeAfterSocialContributions - taxDeductibleExpenses ;
			double taxedIncome0 = Double.parseDouble(wholeNumberFormat.format(taxedIncome));
			System.out.println("income to be taxed = " + taxedIncome
					+ " rounded = " + wholeNumberFormat.format(taxedIncome0));
			double advanceTax = calculateTax(taxedIncome0);
			System.out.println("Advance tax 18 % = "
					+ advanceTax);
			double taxPaid = 0;
			System.out.println("Already paid tax = "
					+ twoDecimalsFormat.format(taxPaid));
			double advanceTaxPaid = calculateAdvanceTax(advanceTax, healthContributionDeductible, taxFreeIncome);
			double advanceTaxPaidRounded = Double.parseDouble(wholeNumberFormat.format(advanceTaxPaid));
			System.out.println("Advance tax  = "
					+ twoDecimalsFormat.format(advanceTaxPaid) + " rounded = "
					+ wholeNumberFormat.format(advanceTaxPaidRounded));
			double netIncome = incomeAfterSocialContributions
					- ((pensionContribution + disabilityContribution + sicknessContribution) + healthContributionFull + advanceTaxPaidRounded);
			System.out.println();
			System.out
					.println("Net income = "
							+ twoDecimalsFormat.format(netIncome));
		} else {
			System.out.println("Unknown type of contract!");
		}
	}

	public static double calculateAdvanceTax(double advanceTax, double healthContributionDeductible, double taxFreeIncome) {
		return advanceTax - healthContributionDeductible - taxFreeIncome;
	}

	public static double calculateTax(double income) {
		return (income * ADVANCE_TAX_RATE_PERCENT) / 100;
	}

	public static double calculatePensionContribution(double income) {
		return (income * PENSION_RATE_PERCENT) / 100;
	}

	public static double calculateDisabilityContribution(double income) {
		return (income * DISABILITY_RATE_PERCENT) / 100;
	}

	public static double calculateSicknessContribution(double income) {
		return (income * SICKNESS_RATE_PERCENT) / 100;
	}

	public static double calculateIncomeAfterSocialContributions(double income) {
		return income
				- calculatePensionContribution(income)
				- calculateDisabilityContribution(income)
				- calculateSicknessContribution(income);
	}

	public static double calculateHealthContributionFull(double income) {
		return (income * HEALTH_CONTRIBUTION_FULL_RATE_PERCENT) / 100;
	}

	public static double calculateHealthContributionDeductible(double income) {
		return (income * HEALTH_CONTRIBUTION_DEDUCTIBLE_RATE_PERCENT) / 100;
	}
}
