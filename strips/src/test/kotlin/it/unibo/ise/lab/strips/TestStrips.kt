package it.unibo.ise.lab.strips

import it.unibo.tuprolog.core.Clause
import it.unibo.tuprolog.core.Integer
import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.parsing.parseAsStruct
import it.unibo.tuprolog.solve.Solution
import it.unibo.tuprolog.solve.SolutionFormatter
import it.unibo.tuprolog.solve.Solver
import it.unibo.tuprolog.solve.channel.OutputChannel
import it.unibo.tuprolog.solve.flags.TrackVariables
import it.unibo.tuprolog.theory.Theory
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertIs

class TestStrips {

    private fun prologPlanner(world: String, maxDepth: Int): Solver =
            Solver.prolog.newBuilder()
                    .staticKb(World.load(world))
                    .dynamicKb(Theory.of(
                            Clause.of(Struct.of("max_depth", Integer.of(maxDepth)))
                    ))
                    .flag(TrackVariables) { ON }
                    .standardOutput(OutputChannel.of { print(it) })
                    .standardError(OutputChannel.of { /* silently ignore */ })
                    .warnings(OutputChannel.of { throw it })
                    .buildMutable()

    private fun testInWorld(world: String, maxDepth: Int = 5, theory: () -> String) {
        val solver = prologPlanner(world, maxDepth)
        val solution = solver.solveOnce(theory().parseAsStruct())

        assertIs<Solution.Yes>(solution)
        println(SolutionFormatter.withOperators(solver.operators).format(solution))
    }

    @Test
    fun testBlockWorld() {
        testInWorld("BlockWorld") {
            """
            strips(
                [on(a, c), ontable(b), ontable(c), clear(a), clear(b), handempty],
                [on(c, b), on(b, a), clear(c)],
                Plan
            )
            """
        }
    }

    @Test
    @Ignore
    fun testRegisters() {
        testInWorld("Registers") {
            """
            strips(
                [register(eax, 0)],
                [register(eax, -2)],
                Plan
            )
            """
        }
    }

    @Test
    @Ignore
    fun testStack() {
        testInWorld("Stack", maxDepth = 7) {
            """
            strips(
                [on(2, on(1, bottom))],
                [on(-1, _)],
                Plan
            )
            """
        }
    }
}