package com.eden.edenbe;

public class TreeNode {
    private UserDTO user;
    private TreeNode left;
    private TreeNode right;
    private int id;

    public TreeNode() {
        this.id = -1;
        this.user = null;
        this.left = null;
        this.right = null;
    }

    public TreeNode(int id, UserDTO user) {
        this.id = id;
        this.user = user;
        this.left = null;
        this.right = null;
    }

    /*
    * getters :
    * */
    public int getId() { return id; }
    public UserDTO getUser() { return user; }
    public TreeNode getLeft() { return left; }
    public TreeNode getRight() { return right; }

    /*
    * setters:
    * */
    public void setId(int id) { this.id = id; }
    public void setUser(UserDTO user) { this.user = user;}
    public void setLeft(TreeNode left) { this.left = left; }
    public void setRight(TreeNode right) { this.right = right; }
}
