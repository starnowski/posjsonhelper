package com.github.starnowski.posjsonhelper.hibernate6;

public abstract class AbstractJsonBExtractPath {}
//extends SqmMapEntryReference<String> implements Serializable {
//
//    private final List<String> path;
//    private final List<Expression> pathValues;
//
//    public AbstractJsonBExtractPath(SqmPath<?> mapPath, NodeBuilder nodeBuilder, List<String> path, String functionName) {
//        super(mapPath, nodeBuilder);
//        this.path = path;
//        if (this.path == null || this.path.isEmpty()) {
//            throw new IllegalArgumentException("Path argument can not be null or empty list");
//        }
//        this.pathValues = this.path.stream().map(p -> new LiteralExpression(criteriaBuilder, p)).collect(Collectors.toList());
//    }
//
//
//    protected Expression<?> getOperand() {
//        return operand;
//    }
//
//    public Class getJavaType() {
//        return operand.getJavaType();
//    }
//
//    public String render(RenderingContext renderingContext) {
//        renderingContext.getFunctionStack().push(this);
//        String var3;
//        try {
//            //TODO Checkin path can be empty (or null) from Postgres perspective
//            var3 = getFunctionName() + "( " + ((Renderable) this.getOperand()).render(renderingContext) + " , " + renderJsonPath(renderingContext) + " )";
//        } finally {
//            renderingContext.getFunctionStack().pop();
//        }
//        return var3;
//    }
//
//    private String renderJsonPath(RenderingContext renderingContext) {
//        StringBuilder sb = new StringBuilder();
//        String sep = "";
//        for (Iterator var11 = pathValues.iterator(); var11.hasNext(); sep = ", ") {
//            Expression value = (Expression) var11.next();
//            sb.append(sep).append(((Renderable) value).render(renderingContext));
//        }
//        return sb.toString();
//    }
//}