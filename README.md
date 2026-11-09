# taxcalculator
TaxCalculator - a kata for a Clean Code exercise

Line numbers refer to the original `TaxCalculator.java`.

## Compilation fixes
- Lines 19, 21: `advanceTaxPaidadvanceTax` declared twice. Replaced by `advanceTax` and `advanceTaxPaid`.
- `advanceTax` was used but never declared. Field added.
- `advanceTaxPaid` was used but never declared. Field added.
- `calculateIncome()`: typo `soc_sick_secur` fixed to `soc_sick_security`.
- Branch C: `taxPaid` was not declared. Declared with value 0 before use.
- Branch C: `income` was read before initialization (reported by javac only after the others were fixed). Local variable renamed to `incomeAfterSocialContributions`.

## Problems

**1. Lines 17-18:** `soc_health1`, `soc_health2` say nothing about meaning.
Renamed to `healthContributionFull` (9%) and `healthContributionDeductible` (7.75%).

**2. Lines 19, 21:** `advanceTaxPaidadvanceTax` merges two names and has no capital between words. Replaced by `advanceTax` and `advanceTaxPaid`.

**3. Line 22:** `advanceTaxPaid0`: the `0` does not say it is the rounded value.
Renamed to `advanceTaxPaidRounded`.

**4. Lines 27, 28, 42, 43:** cryptic names.
`isr` → `inputStreamReader`, `br` → `inputReader`, `df00` → `twoDecimalsFormat`, `df` → `wholeNumberFormat`.

**5. Lines 9-10:** `income` and `contractType` are public static mutable fields (global state).
Moved into `main` as local variables.

**6. Lines 12-14:** `soc_security`, `soc_health_security`, `soc_sick_security` use snake_case and the unclear abbreviation `soc`.
Renamed to `socialSecurity`, `socialHealthSecurity`, `socialSickSecurity`.

**7. Line 48:** the variable `d_income` is assigned but never used.
Removed the variable but kept the call `calculateIncome(income)`, because that method also fills the social contribution fields that are printed later.
*Rule: Needless complexity.*

**8. Line 115:** in the `'C'` branch there was a local variable `advanceTax` that was never used and hid the field with the same name.
Removed.
*Rule: Needless complexity.*

**9. Lines 16, 20, 104 and the `calculate...` methods:** the code had magic numbers (tax rates such as `9.76`, `18`, `20`, and the values `111.25` and `46.33`), so it was not clear what they mean.
Replaced them with named constants, for example `ADVANCE_TAX_RATE_PERCENT` and `EMPLOYMENT_TAX_FREE_INCOME`.
`taxDeductibleExpenses` and `taxFreeIncome` are still normal fields, because the `'C'` branch changes them. They are only initialized with the constants now.
*Rule: Replace magic numbers with named constants.*

**10. Lines 11, 12-14, 15, 17-20:** comments that repeat the code or are misleading (`// health-related taxes` was above variables that are not about health, and `= 0; // ... 9%` looked contradictory).
Removed. The rates are now in named constants, and the variable names explain the rest.
*Rule: Don't be redundant, Always try to explain yourself in code.*

**11. Lines 12-14:** `socialSecurity`, `socialHealthSecurity`, `socialSickSecurity` did not say what each contribution is, and `socialHealthSecurity` (1.5%) is not a health contribution, it is the disability contribution.
Renamed to `pensionContribution` (9.76%), `disabilityContribution` (1.5%), `sicknessContribution` (2.45%), and the same for the rate constants.
The printed texts (for example "Health social security tax") were left unchanged to keep the program output the same.
*Rule: Make meaningful distinction, Choose descriptive and unambiguous names.*

**12. Method `calculateAdvanceTax`:** it had no parameters and read three fields, and wrote its result into a field (side effect).
Now it receives the values as parameters and returns the result.
*Rule: Have no side effects, Prefer fewer arguments only when they are hidden inputs.*

**13. Method `calculateTax`:** it wrote its result into a field instead of returning it.
Now it only returns the result; the caller stores it.
*Rule: Have no side effects.*

**14. Lines 149-152:** `calculateOtherTaxes` calculated two values and wrote them into fields (side effect).
Split into `calculateHealthContributionFull` and `calculateHealthContributionDeductible`, each one returns its result.
*Rule: Have no side effects, Do one thing.*

**15. Lines 142-147:** `calculateIncome` calculated three contributions, wrote them into fields and also returned the income after contributions (side effect and return value mixed).
Split into `calculatePensionContribution`, `calculateDisabilityContribution`, `calculateSicknessContribution` and `calculateIncomeAfterSocialContributions`, each one returns its result.
*Rule: Have no side effects, Do one thing.*