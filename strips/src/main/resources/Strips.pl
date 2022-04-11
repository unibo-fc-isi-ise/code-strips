
:- op(900, xfx, in).

strips(InitState, GoalList, Plan) :-
    strips_impl(InitState, GoalList, [], [], _, RevPlan),
    reverse(RevPlan, Plan).

strips_impl(State, Goal, Plan, _, State, Plan) :-
    subseteq(Goal, State),
    write("Reached goal "), write(Goal), write(" in "), write(State),
    reverse(Plan, ActualPlan),
    write(" through plan "), write(ActualPlan), unindent, newline.

strips_impl(State, Goal, Plan, BadActions, FinalState, FinalPlan) :-
    write("Need to reach "), write(Goal), write(" from "), write(State), indent, newline,
    SubGoal in Goal,
    not(SubGoal in State),
    write('Attempting goal:  '), write(SubGoal), newline,
    action(Action, 'if'(PrecList), '+'(AddList), _, _),
    member(SubGoal, AddList),
    write('Choosing Action:  '), write(Action),
    not(Action in BadActions),
    write(' -- not a bad action.'), newline,
    write('Need to satisfy preconditions of '), write(Action), write(", that are: "), write(PrecList), newline,
    strips_impl(State, PrecList, Plan, [Action | BadActions], TmpState, TmpPlan),
    apply(TmpState, Action, NewState),
    strips_impl(NewState, Goal, [Action | TmpPlan], BadActions, FinalState, FinalPlan).

strips_impl(_, _, _, _, _) :-
    unindent, !, fail.

apply(State, Action, NewState) :-
    write('Simulating '), write(Action), newline,
    write('Transition: '), write(State),
    action(Action, 'if'(PrecList), '+'(AddList), '-'(DelList), where(Conditions)),
    write(' - '), write(DelList),
    difference(State, DelList, TmpState),
    write(" + "),
    subseteq(PrecList, State),
    call(Conditions),
    write(AddList), write(" = "),
    union(AddList, TmpState, NewState),
    write(NewState), newline.
