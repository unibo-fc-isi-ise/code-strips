
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
    /* TODO select an unsatisfied SubGoal in Goal */
    write('Attempting goal:  '), write(SubGoal), newline,
    /* TODO select an Action which may produce SubGoal */
    write('Choosing Action:  '), write(Action),
    /* TODO ensure the selected Action is not blacklisted */
    write(' -- not a bad action.'), newline,
    write('Need to satisfy preconditions of '), write(Action), write(", that are: "), write(PrecList), newline,
    /* TODO check if Action can be applied to the current state */
    /* TODO if not, find a SubPlan making Action applicable, __blacklisting Action__ */
    /* TODO if such a SubPlan exists, let TmpState be the state reached by applying SubPlan to State */
    apply(TmpState, Action, NewState),
    strips_impl(NewState, Goal, [Action | TmpPlan], BadActions, FinalState, FinalPlan).

strips_impl(_, _, _, _, _) :-
    unindent, !, fail.

apply(State, Action, NewState) :-
    write('Simulating '), write(Action), newline,
    write('Transition: '), write(State),
    % TODO get the post-conditions (AddList + DelList), pre-conditions (PrecList), and Conditions of Action
    write(' - '), write(DelList),
    % TODO remove the negative post-conditions (DelList) from State
    write(" + "),
    % TODO ensure all the pre-conditions are satisfied by State
    % TODO perform any additional computation in Conditions
    write(AddList), write(" = "),
    % TODO add the positive post-conditions (AddList) from State
    write(NewState), newline.
