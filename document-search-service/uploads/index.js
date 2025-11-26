import fs from 'fs';

export default class X12 {

    constructor(file) {
        if (file.includes('/')) {
            fs.readFile(file, 'utf8', (err, contents) => {
                return this.parseTextToJSON(contents);
            });
        } else {
            return this.parseTextToJSON(file);
        }
    }

    parseTextToJSON(text) {
        let data = text.split('\n'),
            parsed = {
                "header": {
                    "names": {},
                    "dates": {
                        "requestedShipDate": [],
                        "purchaseOrderDate": []
                    }
                },
                "body": [],
                "summary": {}
            },
            lx = 0,
            typeOf = null;

        data.forEach(segment => {
            let seg = segment.split('*');
            let seg_id = seg[0];
            seg.shift(); // Remove firsts

            if (['ISA', 'GS', 'ST', 'NTE', 'W66', 'W05'].indexOf(seg_id) != -1) {
                parsed['header'][seg_id] = seg
            } else if (['N1', 'N2', 'N3', 'N4'].indexOf(seg_id) != -1) {
                if (seg_id == 'N1') {
                    typeOf = seg[0]
                    parsed['header']['names'][typeOf] = {}
                }
                parsed['header']['names'][typeOf][seg_id] = seg
            } else if (seg_id == 'G62') {
                if (parsed['header']['dates']['requestedShipDate'].length == 0)
                    parsed['header']['dates']['requestedShipDate'] = seg
                else
                    parsed['header']['dates']['purchaseOrderDate'] = seg
            } else if (['W76', 'SE', 'GE', 'IEA'].indexOf(seg_id) != -1) {
                parsed['summary'][seg_id] = seg;
            } else {
                if (seg_id == 'N9' && !parsed['header']['N9'])
                    parsed['header'][seg_id] = seg
                else {
                    if (seg_id == 'LX')
                        lx = (parseInt(seg[0]) - 1);
                    else {
                        if (parsed['body'][lx])
                            parsed['body'][lx][seg_id] = seg;
                        else
                            parsed['body'].push({ [seg_id]: seg });
                    }
                }
            }
        });
        console.log("parsed..." + JSON.stringify(parsed))	
        return parsed;
    }
}
new X12("edi.txt")
