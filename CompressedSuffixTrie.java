/** Written by Mengxin Huang Student ID z5013846**/

import java.io.*;
import java.util.*;

public class CompressedSuffixTrie {
    CSTNode root;
    String str;
    
    // creates a compact representation of the 
    // compressed suffix trie from an input text file f
    public CompressedSuffixTrie(String f) throws IOException {
        File file = new File(f);
        Scanner s = new Scanner(file);
        str = s.useDelimiter("\\Z").next();
        str = str.replaceAll("[^ACGT]", "");
        str += "$";
        root = new CSTNode();
        char[] c = str.toCharArray();

        for (int i = 0; i < c.length; i++) {
            List<Integer> suffix = new ArrayList<Integer>();
            for (int j = i; j < c.length; j++) {
                suffix.add(j);
            }
            root.addNode(suffix, i+1);
        }
        root = compactNode(root, 0);
    }

    // finds a specific string s in the Compressed Suffix Trie
    // if the trie does not contain s, returns -1
    // otherwise, returns the first occurrence of s
    // the time complexity of this method is O(|s|)
    // where |s| is the length of s 
    // firstly, travesal CST to find the node n with the same first character as s
    // if suffix is shorter than or the same as the node lable
    // compares the substring of n and s, if matches, returns the start label of n
    // otherwise, returns -1
    // else if suffix is longer than node label
    // update the suffix length and start index 
    // then compares the child of n repeatly
    // therefore, the time complexity is O(|s|)
    public int findString(String s){
        int len = s.length();
        int j = 0;
        CSTNode node = root;
        do {
            for (CSTNode child : node.children) {
                int i = child.wid.first;
                if (str.charAt(i) == s.charAt(j)) {
                    int k = child.wid.last - i + 1;
                    if (len <= k) {
                        if (str.substring(i, i + len).equals(s.substring(j, j + len)))
                            return i - j;
                        else
                            return -1;
                    }
                    else {
                        if (str.substring(i, i + k).equals(s.substring(j, j + k))) {
                            len = len - k;
                            j = j + k;
                            node = child;
                            break;
                        }
                        else
                            return -1;
                    }
                }
            }
        } while (node.children.size() > 0);
        return -1;
    }
    
    // computes the longest common subsequence of f1 and f2 and write to f2
    // and computes the degree of similarity
    // firstly, convert f1 into a char array c1, f2 into c2 
    // then, creates a two-dimensional int array c
    // loop to compares c1 and c2
    // if c1[i] == c2[j], then c[i][j] = c[i-1][j-1] + 1
    // after comparing all the elements of c1 and c2
    // find the max element in c, that diagonal starts with a non-zero element is the longest common suquence
    // since the size of f1 and f2 is m and n, respectively
    // and there are 3 steps in this method
    // step 1 is O(m*n)
    // step 2 is O(m*n)
    // step 3 is O(m) or O(n)
    // therefore, the total time complextiy should be O(m*n) 
    public static float similarityAnalyser(String f1, String f2, String f3) throws IOException {
        String t1 = new Scanner(new File(f1)).useDelimiter("\\Z").next();
        t1 = t1.replaceAll("[^ACGT]", "");
        char c1[] = t1.toCharArray();
        String t2 = new Scanner(new File(f2)).useDelimiter("\\Z").next();
        t2 = t2.replaceAll("[^ACGT]", "");
        char c2[] = t2.toCharArray();
        int s1 = c1.length;
        int s2 = c2.length;
        //System.out.println(c1);
        //System.out.println(c2); 
        // step 1 O(m*n)
        int[][] c = new int[1000][1000];
        for(int i = 0;i < s1;i++) {
            for(int j = 0;j < s2;j++) {
                if(c1[i] == c2[j]) {
                    if(i == 0 || j == 0)
                        c[i][j] = 1;
                    else if(c[i-1][j-1] == 0)
                        c[i][j] = 1;
                    else
                        c[i][j] = c[i-1][j-1] + 1;
                }
                else
                    c[i][j] = 0;
            }
        }
        // step 2 O(m*n)
        int max = 0;
        int indexI = 0;
        int indexJ = 0;
        for(int i = 0;i < s1;i++) {
            for(int j = 0;j < s2;j++) {
                if(c[i][j] > max) {
                    max = c[i][j];
                    indexI = i;
                    indexJ = j;
                }
            }
        }
        // step 3 O(n) if n < m
        // or O(m) if m < n
        String lcs = "";
        int i = indexI - max + 1;
        int j = indexJ - max + 1;
        while(i <=indexI && j <= indexJ) {
            lcs = lcs + c1[i];
            i++;
            j++;
        }
        FileWriter fw = new FileWriter(f3);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(lcs);
        bw.close();
        int len = lcs.length();
        int smax = s1 >= s2?s1:s2;
        return (float)len / smax;
    }
    
    // Compressed Suffix Trie data structure
    class CSTNode {
        Width wid = null;
        int nodeDepth = 0;
        int label = 0;
        CSTNode parent = null;
        ArrayList<CSTNode> children = null;
        int strDepth;
        int id = 0;

        // default constructer
        public CSTNode() {
            children = new ArrayList<CSTNode>();
            nodeDepth = 0;
            label = 0;
        }

        public CSTNode(CSTNode parent, int first, int last, int dep, int label, int id) {
            children = new ArrayList<CSTNode>();
            wid = new Width(first, last);
            nodeDepth = dep;
            this.label = label;
            this.parent = parent;
            strDepth = parent.strDepth + last - first;
            this.id = id;
        }
        

        public void addNode(List<Integer> suffix, int index) {
            CSTNode node = searchNode(node, suffix);
            insertNode(node, suffix, index);
        }

        public CSTNode searchNode(CSTNode node, List<Integer> suffix) {
            ArrayList<CSTNode> children = node.children;
            for (CSTNode child : children) {
                if (str.charAt(child.wid.first) == str.charAt(suffix.get(0))) {
                    suffix.remove(0);
                    if (suffix.isEmpty()) {
                        return child;
                    }
                    return searchNode(child, suffix);
                }
            }
            return node;
        }

        public void insertNode(CSTNode node, List<Integer> suffix, int index) {
            for (Integer i : suffix) {
                CSTNode child = new CSTNode(node, i, i, node.nodeDepth + 1, index, id);
                node.children.add(child);
                node = child;
            }
        }
    }

    public CSTNode compactNode(CSTNode node, int nodeDepth) {
        node.nodeDepth = nodeDepth;
        for (CSTNode child : node.children) {
            while (child.children.size() == 1) {
                CSTNode grandchild = child.children.get(0);
                child.wid.last = grandchild.wid.last;
                child.children = grandchild.children;
            }
            compactNode(child, nodeDepth + 1);
            }
        return node;
    }
}

// the edge between first character and last character of one string
class Width {
    int first;
    int last;
    public Width(int first, int last) {
        this.first = first;
        this.last = last;
    }
}

