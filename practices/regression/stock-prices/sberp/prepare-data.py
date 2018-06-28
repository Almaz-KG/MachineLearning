def to_string(lists):
    s = ""
    for l in lists:
        s = s +"," + ",".join(l)

    return s


def prepare(input_file, output_file):
    result = []
    with open(input_file) as file:
        lines = file.readlines()

        lst = []
        lastDate = None
        for line in lines:
            tokens = line.strip().split(",")
            date, time, op, high, low, close, vol = tokens

            if lastDate == None:
                lst.append(tokens[1:])
                lastDate = date
                continue

            if lastDate != date:
                result.append([lastDate, lst])
                lst = []
                lst.append(tokens[1:])

            if lastDate == date:
                lst.append(tokens[1:])

            lastDate = date

        result.append([lastDate, lst])

    with open(output_file, "w") as output:
        title = "DATE,TIME1,OPEN1,HIGH1,LOW1,CLOSE1,VOL1,TIME2,OPEN2,HIGH2,LOW2,CLOSE2,VOL2,TIME3,OPEN3,HIGH3,LOW3,CLOSE3,VOL3,TIME4,OPEN4,HIGH4,LOW4,CLOSE4,VOL4,TIME5,OPEN5,HIGH5,LOW5,CLOSE5,VOL5,TIME6,OPEN6,HIGH6,LOW6,CLOSE6,VOL6,TIME7,OPEN7,HIGH7,LOW7,CLOSE7,VOL7,TIME8,OPEN8,HIGH8,LOW8,CLOSE8,VOL8,TIME9,OPEN9,HIGH9,LOW9,CLOSE9,VOL9";
        output.write(title)
        output.write("\n")

        for r in result:
            date = str(r[0])
            st = to_string(r[1])

            output.write(date + str(st) + "\n")





input = "SBERP.csv"
output = "SBERP_prepared.csv"
prepare(input, output)