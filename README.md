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

**12. Lines 134-136, method `calculateAdvanceTax`:** it had no parameters and read three fields, and wrote its result into a field (side effect).
Now it receives the values as parameters and returns the result.
*Rule: Have no side effects.*

**13. Lines 138-140, method `calculateTax`:** it wrote its result into a field instead of returning it.
Now it only returns the result; the caller stores it.
*Rule: Have no side effects.*

**14. Lines 149-152:** `calculateOtherTaxes` calculated two values and wrote them into fields (side effect).
Split into `calculateHealthContributionFull` and `calculateHealthContributionDeductible`, each one returns its result.
*Rule: Have no side effects, Do one thing.*

**15. Lines 142-147:** `calculateIncome` calculated three contributions, wrote them into fields and also returned the income after contributions (side effect and return value mixed).
Split into `calculatePensionContribution`, `calculateDisabilityContribution`, `calculateSicknessContribution` and `calculateIncomeAfterSocialContributions`, each one returns its result.
*Rule: Have no side effects, Do one thing.*

**16. Lines 9-22 (fields):** after the `calculate...` methods stopped writing into fields, the `static` fields were only global state used by `main`.
Replaced them with local variables inside each branch of `main` and removed the fields. `taxDeductibleExpenses` and `taxFreeIncome` are now local to each branch, so branch `'C'` no longer changes shared values.
*Rule: Small number of instance variables.*

**17. Lines 66-67, 78, 109, 119:** the same rounding code `Double.parseDouble(df.format(...))` was repeated four times.
Extracted to the method `roundToWholeNumber`.
*Rule: Needless repetition.*

**18. Lines 25-132:** `main` read the input, did all the calculations and printed everything for both contract types in one method of about 110 lines.
Moved the code of each contract type to its own method (`printEmploymentSummary`, `printCivilSummary`). `main` now only reads the input and chooses the method.
*Rule: Small, Do one thing.*

**19. Lines 66 and 109:** `taxedIncome0`: the `0` does not say it is the rounded value.
Renamed to `taxedIncomeRounded`.
*Rule: Make meaningful distinction.*

**20. Lines 56-58, 62-65, 85-87, 97-99, 104-108, 126-128:** inconsistent formatting: `println` calls split over several lines (`System.out` alone, `);` alone), spaces before `;` and double spaces before `=`.
Joined the split calls into one line and removed the extra spaces. The printed texts were not changed.
*Rule: Be consistent, Follow standard conventions.*

**21. Lines 10, 34, 45, 88:** the contract type was a `char` with the magic values `'E'` and `'C'`, so any letter was accepted by the compiler.
Replaced by the enum `ContractType` (`EMPLOYMENT`, `CIVIL`, `UNKNOWN`) and a `switch` in `main`. Unknown letters still print the same message.
*Rule: Prefer dedicated value objects to primitive type.*

**22. Lines 26-40:** `main` read the input, handled errors and chose the calculation all in one place.
Moved the reading to `readIncome` and `readContractCode`. The `catch (Exception)` was kept on purpose: it also covers end of input and an empty contract type, and narrowing it would change what the program prints.
*Rule: Small, Do one thing.*

**23. Lines 82-83 and 123-124:** the net income formula was written twice, once per contract type.
Moved it to `calculateNetIncome`. The only difference between the two cases (the income used as base) became a parameter.
*Rule: Needless repetition (DRY).*

**24. Lines 83 and 124:** the sum of the three social contributions (`soc_security + soc_health_security + soc_sick_security`) was written twice.
Moved it to `calculateSocialContributionsTotal`.
*Rule: Needless repetition (DRY).*

**25. Lines 104, 139, 143-145, 150-151:** the calculation `(income * rate) / 100` was written seven times.
Moved it to `percentOf` and used it in all the places.
*Rule: Needless repetition (DRY).*

**26. Lines 134-140 (methods `calculateAdvanceTax` and `calculateTax`):** the two names were too similar and did not say what each method returns. `calculateTax` returned the advance tax and `calculateAdvanceTax` returned the advance tax paid. Its parameter `income` was in fact the taxed income.
Renamed to `calculateAdvanceTax(taxedIncome)` and `calculateAdvanceTaxPaid`, the same names as the variables that store their results.
*Rule: Make meaningful distinction, Choose descriptive and unambiguous names.*

**27. Lines 42-43:** the two `DecimalFormat` objects were created in `main` and passed to the methods that print, so each method received two extra arguments.
Made them constants of the class (`TWO_DECIMALS_FORMAT`, `WHOLE_NUMBER_FORMAT`). `printEmploymentSummary` and `printCivilSummary` now receive only the income, and `roundToWholeNumber` uses the same constant instead of creating a new one every time.
*Rule: Prefer fewer arguments.*

**28. Lines 46-87 and 89-128:** in both contract types, calculations and `println` calls were mixed line by line.
Each print method now calculates everything first and prints afterwards, in the same order as before. The output is the same.
*Rule: Separate concepts vertically, Declare variables close to their usage.*

**29. Lines 45-131:** the code of each contract type was in a `static` method that received the income as an argument.
Moved each one to its own class (`EmploymentContract`, `CivilContract`) with the income as a field and a `printSummary()` method. The code inside was not changed.
*Rule: Prefer non-static methods to static methods, Prefer fewer arguments.*

**30. Lines 46-87 and 89-128:** `printSummary` in each contract class still calculated about 13 values before printing them, so it was long and did two things.
Moved each calculation to its own small method (`taxedIncome`, `taxedIncomeRounded`, `advanceTax`, `advanceTaxPaid`, `netIncome`, ...). `printSummary` now only prints. In `CivilContract`, `taxFreeIncome = 0` and `taxPaid = 0` became constants of the class.
*Rule: Small, Do one thing.*

## Not changed

- `printSummary` in `EmploymentContract` and `CivilContract` still calculates and prints in the same method, and the two have a similar structure.
- `main` still chooses the contract with a `switch` on `ContractType`. Replacing it with polymorphism was possible, but it adds more types for a `switch` with only three cases.
- The `catch (Exception)` in `main` was kept to keep the same behavior (see 22).
- Printed texts were not changed (for example "Health social security tax" prints the disability value, see 11).