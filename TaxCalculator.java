package com.bartoszwalter.students.taxes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class TaxCalculator {
	private static final double SOCIAL_SECURITY_RATE_PERCENT = 9.76;
	private static final double SOCIAL_HEALTH_SECURITY_RATE_PERCENT = 1.5;
	private static final double SOCIAL_SICK_SECURITY_RATE_PERCENT = 2.45;
	private static final double HEALTH_CONTRIBUTION_FULL_RATE_PERCENT = 9;
	private static final double HEALTH_CONTRIBUTION_DEDUCTIBLE_RATE_PERCENT = 7.75;
	private static final double ADVANCE_TAX_RATE_PERCENT = 18;
	private static final double CIVIL_DEDUCTIBLE_EXPENSES_RATE_PERCENT = 20;
	private static final double EMPLOYMENT_DEDUCTIBLE_EXPENSES = 111.25;
	private static final double EMPLOYMENT_TAX_FREE_INCOME = 46.33;
	// social security taxes
	public static double socialSecurity = 0; // 9,76% of basis
	public static double socialHealthSecurity = 0; // 1,5% of basis
	public static double socialSickSecurity = 0; // 2,45% of basis
	// health-related taxes
	public static double taxDeductibleExpenses = EMPLOYMENT_DEDUCTIBLE_EXPENSES;
	public static double healthContributionFull = 0; // of basis up to 9%
    public static double healthContributionDeductible = 0; // of basis up to  7,75 %
    public static double advanceTax = 0;
    public static double advanceTaxPaid = 0; // advance tax 18%
    public static double advanceTaxPaidRounded = 0; // advance tax 18%
    public static double taxFreeIncome = EMPLOYMENT_TAX_FREE_INCOME;

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
			calculateIncome(income);
			System.out.println("Social security tax "
					+ twoDecimalsFormat.format(socialSecurity));

			System.out.println("Health social security tax    "
					+ twoDecimalsFormat.format(socialHealthSecurity));
			System.out.println("Sickness social security tax  "
					+ twoDecimalsFormat.format(socialSickSecurity));
			System.out
					.println("Income basis for health social security: "
							+ income);
			calculateOtherTaxes(income);
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
			calculateTax(taxedIncome0);
			System.out.println("Advance tax 18 % = "
					+ advanceTax);
			System.out.println("Tax free income = " + taxFreeIncome);
			double taxPaid = advanceTax - taxFreeIncome;
			System.out.println("Reduced tax = "
					+ twoDecimalsFormat.format(taxPaid));
			calculateAdvanceTax();
			advanceTaxPaidRounded = Double.parseDouble(wholeNumberFormat.format(advanceTaxPaid));
			System.out.println("Advance tax paid = "
					+ twoDecimalsFormat.format(advanceTaxPaid) + " rounded = "
					+ wholeNumberFormat.format(advanceTaxPaidRounded));
			double netIncome = income
					- ((socialSecurity + socialHealthSecurity + socialSickSecurity) + healthContributionFull + advanceTaxPaidRounded);
			System.out.println();
			System.out
					.println("Net income = "
							+ twoDecimalsFormat.format(netIncome));
		} else if (contractType == 'C') {
			System.out.println("income " + income);
			double incomeAfterSocialContributions = calculateIncome(income);
			System.out.println("Social security tax = "
					+ twoDecimalsFormat.format(socialSecurity));
			System.out.println("Health social security tax = "
					+ twoDecimalsFormat.format(socialHealthSecurity));
			System.out.println("Sickness social security tax = "
					+ twoDecimalsFormat.format(socialSickSecurity));
			System.out
					.println("Income for calculating health security tax: "
							+ incomeAfterSocialContributions);
			calculateOtherTaxes(incomeAfterSocialContributions);
			System.out.println("Health security tax: 9% = "
					+ twoDecimalsFormat.format(healthContributionFull) + " 7,75% = " + twoDecimalsFormat.format(healthContributionDeductible));
			taxFreeIncome = 0;
			taxDeductibleExpenses  = (incomeAfterSocialContributions * CIVIL_DEDUCTIBLE_EXPENSES_RATE_PERCENT) / 100;
			System.out.println("Tax deductible expenses = "
					+ taxDeductibleExpenses
			);
			double taxedIncome = incomeAfterSocialContributions - taxDeductibleExpenses ;
			double taxedIncome0 = Double.parseDouble(wholeNumberFormat.format(taxedIncome));
			System.out.println("income to be taxed = " + taxedIncome
					+ " rounded = " + wholeNumberFormat.format(taxedIncome0));
			calculateTax(taxedIncome0);
			System.out.println("Advance tax 18 % = "
					+ advanceTax);
            double taxPaid = 0;
			System.out.println("Already paid tax = "
					+ twoDecimalsFormat.format(taxPaid));
			calculateAdvanceTax();
			advanceTaxPaidRounded = Double.parseDouble(wholeNumberFormat.format(advanceTaxPaid));
			System.out.println("Advance tax  = "
					+ twoDecimalsFormat.format(advanceTaxPaid) + " rounded = "
					+ wholeNumberFormat.format(advanceTaxPaidRounded));
			double netIncome = incomeAfterSocialContributions
					- ((socialSecurity + socialHealthSecurity + socialSickSecurity) + healthContributionFull + advanceTaxPaidRounded);
			System.out.println();
			System.out
					.println("Net income = "
							+ twoDecimalsFormat.format(netIncome));
		} else {
			System.out.println("Unknown type of contract!");
		}
	}

	public static void calculateAdvanceTax() {
		advanceTaxPaid = advanceTax - healthContributionDeductible - taxFreeIncome;
	}

	public static void calculateTax(double income) {
		advanceTax = (income * ADVANCE_TAX_RATE_PERCENT) / 100;
	}

	public static double calculateIncome(double income) {
		socialSecurity = (income * SOCIAL_SECURITY_RATE_PERCENT) / 100;
		socialHealthSecurity = (income * SOCIAL_HEALTH_SECURITY_RATE_PERCENT) / 100;
		socialSickSecurity = (income * SOCIAL_SICK_SECURITY_RATE_PERCENT) / 100;
		return (income - socialSecurity - socialHealthSecurity - socialSickSecurity);
	}

	public static void calculateOtherTaxes(double income) {
		healthContributionFull = (income * HEALTH_CONTRIBUTION_FULL_RATE_PERCENT) / 100;
		healthContributionDeductible = (income * HEALTH_CONTRIBUTION_DEDUCTIBLE_RATE_PERCENT) / 100;
	}
}
