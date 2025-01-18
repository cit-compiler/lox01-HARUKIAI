package com.craftinginterpreters.lox;

class AstPrinter implements Expr.Visitor<String> {
    // Expr を文字列に変換するメソッド
    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        // 代入式の文字列を生成
        return parenthesize("assign " + expr.name.lexeme, expr.value);
    }

    // 親子関係を表すために文字列を括弧で括る
    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }

    // メイン関数で AST を生成して表示
    public static void main(String[] args) {
        // AST の構築例
        Expr expression = new Expr.Binary(
            new Expr.Unary(
                new Token(TokenType.MINUS, "-", null, 1),
                new Expr.Literal(123)),
            new Token(TokenType.STAR, "*", null, 1),
            new Expr.Grouping(
                new Expr.Literal(45.67)));

        // AST を文字列に変換して出力
        System.out.println(new AstPrinter().print(expression));
    }
}
