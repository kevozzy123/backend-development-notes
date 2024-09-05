
Refer to this awesome article by algomonster for an introduction to backtracking: https://algo.monster/problems/dfs_with_states
Many backtracking problems can be solved using this template:
```java
    // There may be some other necessary arguments here
    // For example, the starting point of the index
    dfs(List<List<Integer>> res, List<Integer> list, int[] nums) {
        if(terminationConditionIsMet) {
            // do something, probably add the current subset to result
            res.add(list);
            return;
        } else {
            for(int i = start; i < nums.length; i++) {
                if(needsPruning) continue;
                list.add(nums[i]);
                dfs(res, list, nums);
                // backtrack
                list.remove(list.size()-1);
            }
        }
    }
```

Let's first take a look at problem 46 to see this template in action:
### 46. Permutations
https://leetcode.com/problems/permutations/
This is probably the entry level problems of backtracking. The template above will perfectly suffice.
```java
class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        helper(res, new ArrayList(), nums);
        return res;
    }

    void helper(List<List<Integer>> res, List<Integer> list, int[] nums) {
        if(list.size() == nums.length) {
            res.add(new ArrayList(list));
            return;
        }
        for(int i = 0; i < nums.length; i++) {
            if(list.contains(nums[i])) {
                continue;
            }
            list.add(nums[i]);
            helper(res, list, nums);
            list.remove(list.size()-1);
        }
    }
}
```
### 39. Combination Sum
https://leetcode.com/problems/combination-sum/

```java
class Solution {
    public List<List<Integer>> combinationSum(int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        helper(res, new ArrayList(), nums, 0, target);
        return res;
    }

    void helper(List<List<Integer>> res, List<Integer> list, int[] nums, int start, int r) {
        if(r < 0) {
            return;
        }
        if(r == 0) {
            res.add(new ArrayList(list));
            return;
        } 
        for(int i = start; i < nums.length; i++) {
            if(nums[i] > r) continue;
            list.add(nums[i]);
            helper(res, list, nums, i, r-nums[i]);
            list.remove(list.size()-1);
        }
    }
}
```

### 40. Combination Sum II
https://leetcode.com/problems/combination-sum-ii/description/
The pruning strategy is what makes this one tricky, for a more comprehensive guide, read this blog article: https://programmercarl.com/0040.%E7%BB%84%E5%90%88%E6%80%BB%E5%92%8CII.html#%E6%80%9D%E8%B7%AF

In essence, we want to allow duplicated numbers, as there are duplicates in the original array. However, we don't want duplicate combinations. 
![image-20221229144232651](../../my_imgs/pruning.png)

The difficulty arises from the fact that "used" elements can refer to two different contexts in a tree-like structure:

Used in the same branch: This refers to elements that have been used in the same combination (i.e., along the same path of the tree).
Used in the same level: This refers to elements that have been used at the same depth or level of the tree but in different branches.

pruning should be applied at the same tree level, not within the same branch. This is because, within a branch, elements are part of the same combination, so there's no need to avoid duplicates. However, at the same tree level, avoiding duplicates ensures that no two different branches (or combinations) are identical.
* used[i - 1] == true means that candidates[i - 1] was used in the same branch.
* used[i - 1] == false indicates that candidates[i - 1] was used on the same tree level.
Some readers might wonder why used[i - 1] == false signifies the same tree level. The reason is that only when used[i - 1] == false can it indicate that the current candidates[i] was selected after backtracking from candidates[i - 1] on the same tree level.

Summary: In the case of [1,1,2], we aim to eliminate duplicate combinations like 1,1*,2 and 1*,1,2. To achieve this, pruning should occur at the same tree level. A boolean[] array can be used to track which elements have been used. If we encounter a node that is identical to the previous one on the same level, and the previous one hasn't been used, it indicates that we should prune this branch to remove the duplicate combination.

```java
class Solution {
    public List<List<Integer>> combinationSum2(int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        dfs(res, new ArrayList(), nums, 0, target);
        return res;
    }

    void dfs(List<List<Integer>> res, List<Integer> list, int[] nums, int start, int r) {
        if(r < 0) return;
        if(r == 0) {
            res.add(new ArrayList(list));
        } else {
            for(int i = start; i < nums.length; i++) {
                if(i > start && nums[i] == nums[i-1]) {
                    continue;
                }
                list.add(nums[i]);
                dfs(res, list, nums, i+1, r-nums[i]);
                list.remove(list.size()-1);
            }
        }
    }
}
```

### 90. Subsets II
https://leetcode.com/problems/subsets-ii/description/
This one uses a very similar pruning strategy at **40. Combination Sum II**

```java
class Solution {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        dfs(res, new ArrayList(), nums, 0);
        return res;
    }

    void dfs(List<List<Integer>> res, List<Integer> list, int[] nums, int start) {
        res.add(new ArrayList(list));
        for(int i = start; i < nums.length; i++) {
            if(i > start && nums[i] == nums[i-1]) continue;

            list.add(nums[i]);
            dfs(res, list, nums, i+1);
            list.remove(list.size()-1);
        }
    }
}
```

### 216. Combination Sum III
https://leetcode.com/problems/combination-sum-iii/description/
```java
class Solution {
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, new ArrayList(), k, n, 1);
        return res;
    }

    void dfs(List<List<Integer>> res, List<Integer> list, int k, int n, int start) {
        if(n < 0) return;
        if(list.size() == k && n == 0) {
            res.add(new ArrayList(list));
        } else {
            for(int i = start; i <= 9; i++) {
                list.add(i);
                dfs(res, list, k, n - i, i+1);
                list.remove(list.size()-1);
            }
        }
    }
}
```

### 47. Permutations II
https://leetcode.com/problems/permutations-ii/description/

```java
class Solution {
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        dfs(res, new ArrayList(), nums, new boolean[nums.length]);
        return res;
    }

    void dfs(List<List<Integer>> res, List<Integer> list, int[] nums, boolean[] used) {
        if(list.size() == nums.length) {
            res.add(new ArrayList(list));
        } else {
            for(int i = 0; i < nums.length; i++) {
                // used[i]: straightforward, we are avoiding using the same element
                // nums[i] == nums[i-1] && !used[i-1]: we are checking
                if(used[i] || i > 0 && nums[i] == nums[i-1] && !used[i-1]) {
                    continue;
                }
                list.add(nums[i]);
                used[i] = true;
                dfs(res, list, nums, used);
                used[i] = false;
                list.remove(list.size()-1);
            }
        }
    }
}
```

### 77. Combinations
```java
class Solution {
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, new ArrayList(), 1,n,k);
        return res;
    }

    void dfs(List<List<Integer>> res, List<Integer> list, int start, int n, int k) {
        if(list.size() == k) {
            res.add(new ArrayList(list));
        } else {
            for(int i = start; i <= n; i++) {
                list.add(i);
                dfs(res,list,i+1,n,k);
                list.remove(list.size()-1);
            }
        }
    }
}
```

### 78. Subsets
https://leetcode.com/problems/subsets/description/
```java
class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, new ArrayList(), nums, 0);
        return res;
    }

    void dfs(List<List<Integer>> res, List<Integer> list, int[] nums, int start) {
        res.add(new ArrayList(list));
        for(int i = start; i < nums.length; i++) {
            list.add(nums[i]);
            dfs(res, list, nums, i+1);
            list.remove(list.size()-1);
        }
    }
}
```

### 491. Non-decreasing Subsequences
https://leetcode.com/problems/non-decreasing-subsequences/description/
```java
class Solution {
    public List<List<Integer>> findSubsequences(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, new ArrayList(), nums, 0);
        return res;
    }

    void dfs(List<List<Integer>> res, List<Integer> list, int[] nums, int start) {
        if (list.size() > 1) {
            res.add(new ArrayList(list));
        }
        Set<Integer> used = new HashSet<>();
        for (int i = start; i < nums.length; i++) {
            if (used.contains(nums[i]))
                continue;
            if (list.isEmpty() || nums[i] >= list.get(list.size() - 1)) {
                used.add(nums[i]);
                list.add(nums[i]);
                dfs(res, list, nums, i + 1);
                list.remove(list.size() - 1);
            }
        }
    }
}
```

### Restore IP address
https://leetcode.com/problems/restore-ip-addresses/

```java
class Solution {
    public List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        dfs(res, new ArrayList(), s, 0, s.length());
        return res;
    }

    void dfs(List<String> res, List<String> list, String s, int start, int remain) {
        if(list.size() == 4 && remain > 0) {
            return;
        } else if(list.size() == 4 && remain == 0) {
            res.add(String.join(".", list));
            return;
        }
        for(int i = start; i < s.length(); i++) {
            String str = s.substring(start, i+1);
            if(isValid(str)) {
                list.add(str);
                dfs(res, list, s, i+1, remain - str.length());
                list.remove(list.size()-1);
            }
        }
    }

    boolean isValid(String s) {
        if(s.length() > 3) return false;
        if(s.length() > 1 && s.charAt(0) == '0') return false;
        int num = Integer.parseInt(s);
        if(num > 255) {
            return false;
        }
        return true;
    }
}
```

### 131. Palindrome Partitioning
https://leetcode.com/problems/palindrome-partitioning/description/
```java
class Solution {
    public List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        dfs(res, new ArrayList(), 0, s);
        return res;
    }

    void dfs(List<List<String>> res, List<String> list, int start, String str) {
        if(start == str.length()) {
            res.add(new ArrayList(list));
        } else {
            for(int i = start; i < str.length(); i++) {
                String sub = str.substring(start, i+1);
                if(isPalin(sub)) {
                    list.add(sub);
                    dfs(res, list, i+1, str);
                    list.remove(list.size()-1);
                }
            }
        }
    }

    private boolean isPalin(String s) {
        int i = 0;
        int j = s.length()-1;
        while(i < j) {
            if(s.charAt(i) != s.charAt(j)) {
                return false;
            }
            i++;
            j--;
        }

        return true;
    }
}
```

### 22. Generate Parentheses
https://leetcode.com/problems/generate-parentheses/description/

```java
class Solution {
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        helper(res, "", 0, 0, n);
        return res;
    }

    void helper(List<String> list, String str, int l, int r, int n) {
        if(str.length() == n*2) {
            list.add(str);
            return;
        }
        /**
         * Note that the following commented out code wouldn't work,
         * because strings are immutable in java. This means that the second
         * if block will be dealing with polluted string, rather than the original string.
         */
        // if(l < n) {
        //     str += "(";
        //     helper(list, str, l+1, r, n);
        // }
        // if(r < l) {
        //     str += ")";
        //     helper(list, str, l, r+1, n);
        // }
        if(l < n) {
            helper(list, str+"(", l+1, r, n);
        }
        if(r < l) {
            helper(list, str+")",l,r+1,n);
        }
    }
}
```


### 51. N-Queens
https://leetcode.com/problems/n-queens/description/

```java
class Solution {
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>();
        char[][] board = new char[n][n];
        for(char[] row : board) {
            Arrays.fill(row, '.');
        }
        boolean[] col = new boolean[n];
        boolean[] left = new boolean[n*2-1];
        boolean[] right = new boolean[n*2-1];
        dfs(0, n, board, res, col, left, right);
        return res;
    }

    void dfs(int i, int n, char[][] board, List<List<String>> res, boolean[] col,
        boolean[] left, boolean[] right) {
            if(i == n) {
                List<String> list = new ArrayList<>();
                for(char[] row : board) {
                    list.add(new String(row));
                }
                res.add(list);
                return;
            }
            // i represents row, j represents column
            for(int j = 0; j < board.length; j++) {
                if(col[j] || left[i+j] || right[n-i+j-1]) {
                    continue;
                }
                col[j] = left[i+j] = right[n-i+j-1] = true;
                board[i][j] = 'Q';
                dfs(i+1, n, board, res, col, left, right);
                col[j] = left[i+j] = right[n-i+j-1] = false;
                board[i][j] = '.';
            }
    }
}
```

### 52. N-Queens II
https://leetcode.com/problems/n-queens-ii/description/

```java
class Solution {
    int res = 0;
    public int totalNQueens(int n) {
        char[][] board = new char[n][n];
        for(char[] row : board) {
            Arrays.fill(row, '.');
        }
        boolean[] col = new boolean[n];
        boolean[] left = new boolean[2*n -1];
        boolean[] right = new boolean[2*n -1];

        dfs(0, n, col, left, right);

        return res;
    }

    void dfs(int i, int n, boolean[] col, boolean[] left, boolean[] right) {
        if(i == n) {
            res++;
            return;
        }
        
        for(int j = 0; j < n; j++) {
            if(col[j] || left[i+j] || right[n-i+j-1]) {
                continue;
            }
            col[j] = left[i+j] = right[n-i+j-1] = true;
            dfs(i+1, n, col, left, right);
            col[j] = left[i+j] = right[n-i+j-1] = false;
        }
    }
}
```

### 37. Sudoku Solver
https://leetcode.com/problems/sudoku-solver/description/

```java
class Solution {
    public void solveSudoku(char[][] board) {
        backtrack(board);
    }

    boolean backtrack(char[][] board) {
        int n = board.length;
        for (int i = 0; i < n; i++) { // row
            for (int j = 0; j < n; j++) { // column
                if (board[i][j] == '.') {
                    // check the value we are going to put in this cell
                    for (int k = 1; k <= 9; k++) { 
                        if (isValid(i, j, k, board)) { // is this number works
                            board[i][j] = Character.forDigit(k, 10); // convert char to int
                            if (backtrack(board)) return true;
                            board[i][j] = '.';
                        }
                    }
                    // return false early if nothing works for this cell
                    return false;
                }
            }
        }
        return true;
    }

    boolean isValid(int row, int col, int num, char[][] board) {
        int n = board.length;
        int val = Character.forDigit(num, 10);
        // check row
        for (int i = 0; i < n; i++) {
            if(board[row][i] == val) {
                return false;
            }
        }

        // check column
        for (int i = 0; i < n; i++) {
            if(board[i][col] == val) {
                return false;
            }
        }
        int startRow = (row/3)*3;
        int startCol = (col/3)*3;
        // check the 3 by 3 rectangle
        for(int i = startRow; i < startRow+3; i++) {
            for(int j = startCol; j < startCol+3; j++) {
                if(board[i][j] == val) {
                    return false;
                }
            }
        }

        return true;
    }
}
```