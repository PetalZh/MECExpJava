print("Hello world")


def plotENBar():
    theta = "22.0"  # 14
    x = ["200", "300", "400", "500", "600", "800", "1000", "1500", "2000", "2500", "3042"]
    # x = ["200", "500", "1000", "1500", "2000", "2500", "2768"]
    plot_dict = {}

    for method in dict[theta]:
        for i in x:
            if method in plot_dict:
                if i in dict[theta][method]:
                    plot_dict[method].append(dict[theta][method][i][0])
                else:
                    plot_dict[method].append(0)
            else:
                plot_dict.update({method: [dict[theta][method][i][0]]})

    print(plot_dict)

    # fig,ax = plt.subplots()
    fig = plt.figure(figsize=(8, 6))

    plt.xticks(fontsize=13)
    plt.yticks(fontsize=24)

    plt.xlabel(r'Number of BS', fontsize=font)
    plt.ylabel(r'# of EN selected', fontsize=font)

    r = range(1, len(plot_dict['greedy']) + 1)
    p_greedy = [i - 0.2 for i in r]
    p_greedy_new = [i for i in r]
    p_mip = [i + 0.2 for i in r]
    p_mip_cluster = [i + 0.4 for i in r]
    # print(p_random)

    #     bar1 = plt.bar(p_random, height = plot_dict['random'], width = 0.2, alpha = 0.8, color = 'y',label = 'Random')
    bar2 = plt.bar(p_greedy, plot_dict['greedy'], width=0.2, alpha=0.8, color='g', label='CFS')
    bar3 = plt.bar(p_greedy_new, plot_dict['greedy_new'], width=0.2, alpha=0.8, color='y', label='DA-CFS')
    bar3 = plt.bar(p_mip, plot_dict['mip_cluster'], width=0.2, alpha=0.8, color='r', label='MIP+cluster')
    bar4 = plt.bar(p_mip_cluster[0:11], plot_dict['mip'][0:11], width=0.2, alpha=0.8, color='b', label='MIP')

    plt.xticks(r, x)
    plt.legend(bbox_to_anchor=(0.39, 1), loc=1, borderaxespad=0., fontsize=18)

    # plt.grid(True)
    plt.savefig('./img/en.pdf', bbox_inches='tight')


plotENBar()
