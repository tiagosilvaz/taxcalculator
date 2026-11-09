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

**7. Line 48:** `d_income` is assigned but never used.
Removed the variable; the call to `calculateIncome(income)` is kept because it sets the social contribution fields.

**8. Line 115:** local variable `advanceTax` in branch `'C'` is declared but never used. Removed.