import graphviz

f = graphviz.Digraph('finite_state_machine', filename='Lab1GraphViz.gv')
f.attr(rankdir='LR', size='8,5')


print("Enter rules, when ready type \"Exit\" ")
verticesMap = {}

while True:
    val = input()
    array = val.split(" ")
    if val == "exit" or val == "Exit":
        break
    else:
        if len(array) == 3:  # S aB

            f.attr('node', shape='circle')
            f.edge(array[0], array[2], label=array[1])


f.view()

