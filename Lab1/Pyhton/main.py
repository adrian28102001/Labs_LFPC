import graphviz

f = graphviz.Digraph('finite_state_machine', filename='FinalAutomation.gv')
f.attr(rankdir='LR', size='8,5')
f.node('Start', shape='plaintext')
f.edge("Start", "q0")

print("Enter production rules, when ready type \"Exit\" ")
verticesMap = {}


while True:
    val = input()
    if val == "exit" or val == "Exit" or val == "EXIT":
        break
    else:
        if len(val) == 4:  # S aB
            if val[0] not in verticesMap.keys():
                verticesMap[val[0]] = "q" + str(len(verticesMap))

            if val[3] not in verticesMap.keys():
                verticesMap[val[3]] = "q" + str(len(verticesMap))

            f.attr('node', shape='circle')
            f.edge(verticesMap.get(val[0]), verticesMap.get(val[3]), label=val[2])

        else:  # B b
            if val[0] not in verticesMap.keys():
                verticesMap[val[0]] = "q" + str(len(verticesMap))
            if val[2] not in verticesMap.keys():
                verticesMap[val[2]] = "q" + str(len(verticesMap))
            f.attr('node', shape='doublecircle')
            f.node(verticesMap.get(val[2]))
            f.edge(verticesMap.get(val[0]), verticesMap.get(val[2]), label=val[2])

f.view()
