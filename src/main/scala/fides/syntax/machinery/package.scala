package fides.syntax

/**
  * This package provides the (phantom) type machinery used to express the syntax of Fides.
  *
  * There are four main levels of "types":
  * <ol>
  * <li> <b>Data types</b> characterize data/values (subtypes of [[TopD]]).
  * <li> <b>Grammar types</b> characterize code, except for escapes (subtypes of [[TopG]]).
  * <li> <b>Meta types</b>, also called scapes, fully characterize code, including escapes (subtypes of [[TopM]]).
  * <li> <b>Code types</b> represent actual code and can be instantiated (subtypes of [[Code]]).
  * </ol>
  * <br>
  * By convention, in a type definition, the type parameters that matter are written
  * on the last line of the type parameter list, while all the parameters above are for bookkeeping.
  * <br><br>
  * (Note: in Fides there is a bijection between terms and types.)
  */
package object machinery
