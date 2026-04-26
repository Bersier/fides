## The Negation Extension of a Poset

### Poset with negation

A poset $Q$ with a map $- : Q \to Q$ satisfying:
1. **Involution:** $-(-s) = s$.
2. **Order-reversal:** $s \le t \Rightarrow -t \le -s$.
3. **Comparability:** $s$ and $-s$ are comparable.

---

### Positive cone

For a poset with negation $Q$, its *positive cone* is the canonically determined subset
$$Q^{+} \;:=\; \{x \in Q : {-x} \le x\}.$$

---

### Downward negation extension

Given a poset $P$, the *downward negation extension* of $P$ is a poset with negation $N(P)$ together with an order-embedding $i : P \hookrightarrow N(P)$ such that $i(P) = N(P)^{+}$, satisfying:
> **Universal property (terminal)**
> 
> For any poset with negation $Q$ and any order-embedding $f : P \to Q$ with $f(P) = Q^{+}$, there exists a unique monotone map $\tilde{f} : Q \to N(P)$ such that
> $$\tilde{f} \circ f = i \qquad\text{and}\qquad \tilde{f}(-x) = -\tilde{f}(x) \text{ for all } x \in Q.$$
