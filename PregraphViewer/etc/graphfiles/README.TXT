This directory contains a small set of files created using the pregraphs
generator and the admissable_c and c4cover filtering program. For each
class all non-empty sets of fixed order from 1 to 10 are included. The
names of the files follow this naming scheme:

    pregraphs_TYPE_ORDER.code

In this name TYPE and ORDER are replaced with the type and order respectively
of the pregraphs in that file. The type is a collection of letters and maybe
a 4. The meaning of these letters are explained below.

    C   cubic simple graphs
    L   pregraphs with possible loops
    S   pregraphs with possible semi-edges
    M   pregraphs with possible multi-edges
    c   only 3-edge-colourable pregraphs
    B   only bipartite pregraphs
    q   only pregraphs that have a 2-factor where each component is a
        quotient of a cycle of order 4
    4   only pregraphs that have a 2-factor where each component is a
        cycle of order 4

All these files are in the pregraph_code format. See
http://caagt.ugent.be/pregraphs for more information.