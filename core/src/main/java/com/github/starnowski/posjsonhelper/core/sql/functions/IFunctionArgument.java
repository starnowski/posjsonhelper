package com.github.starnowski.posjsonhelper.core.sql.functions;

public interface IFunctionArgument {
    //( [ [ argmode ] [ argname ] argtype [ { DEFAULT | = } default_expr ] [, ...] ] )

    String getType();
}
