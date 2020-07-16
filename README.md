# GameOfThrones
冰与火之歌综合信息安卓版APP

## Design 
&emsp;&emsp;The project is an android app on the topic of Game of Thrones. The app mainly provides information in three aspects.<br> 
- ### Family Info <br>
&emsp;&emsp;First, it provides information about some important characters classified by families. Data in this part mainly comes from the "Game of thrones"Wiki api, the data it returns is in the format of "XML". <br>
- ### Book Info <br>
&emsp;&emsp;Second, users can get information about the book series which has released, including five chapters. Data in this part mainly comes from "douban" api and "An API of Ice And Fire", which provides character data, book data and building data in the world of "Ice and Fire". Both apis return json data <br>
- ### Music Info <br>
&emsp;&emsp;Third, the app also provides information about the music in the TV series. Data in this part mainly comes from "CloudMusic" api. The api returns data in the format of json.
<br><br>

## Implementation
&emsp;&emsp;On the whole, the project can be devided into three parts. First, we need to receive json(xml) data from api and then parse them to get what we need to display on the app. Second, I need to create some class to store the parsed data. Finally, we need to display the parsed data in a pleasing manner. <br>
### 1. Coding
- Get parsed data <br>
&emsp;&emsp;As to this part, it can be devided into two steps. First, I need to get json(xml) data from api. This step is necessary for all the data analysis and the processes are the same. Therefore, I create a class called <font color=red> "GetData"</font>. The two primary methods (getJson,getXML) in this class are both static, so all objects can call these two functions directly. Then, it comes to the class called <font color=red>"DataAnalysis"</font> which is used to parse json(xml) data. Different apis need to be analyzed respectly, since the data are shown in different forms, and I need to grasp what I need.<br>
```java
public class GetData {
    //以httpUrlConnection的方式获取api数据，并返回流（适用于XML）
    public static InputStream getXML(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        //conn.setRequestMethod("GET");
        int code=conn.getResponseCode();
        if (code == 200) {
            InputStream in = conn.getInputStream();
            return in;
        }
        else{
            Log.d("DisplayXML","error!!! :");
        }
        return null;
    }
    //以httpUrlConnection的方式获取api数据，并返回字符（适用于json）
    public static String getJson(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        //conn.setRequestMethod("GET");
        int code=conn.getResponseCode();
        if (code == 200) {
            InputStream in = conn.getInputStream();
            byte[] data = read(in);
            String apiData = new String(data, "UTF-8");
            return apiData;
        }
        else{
            InputStream in = conn.getInputStream();
            byte[] data = read(in);
            String json = new String(data, "UTF-8");
            Log.d("Json","error!!! :"+json);
        }
        return null;
    }
``` 
<br>

- Data storage<br>
 &emsp;&emsp;As I have said, the app mainly provides infomation in three aspects: family, book and music. Therefore, it's nature that I need to create three related classes. In my project, I call them <font color=red> "Family", "Book" and "Album". </font> <br>
 &emsp;&emsp;To optimize my app, I use *singleton* design pattern when designing these three classes, which ensures an object only has one instance. <br>
 *The following code comes from Famiy.java*:<br>
 ```java
 public class Family {
    boolean haveLoaded=false;
    public  String familyName;
    public  ArrayList<Characters> top10Person = new ArrayList<Characters>();
    public  ArrayList<ImageAndText> pages = new ArrayList<ImageAndText>();

    public static Map<String,Family> instance=new HashMap<>();

    public static Family getInstance(String name){
        if(instance.get(name)==null){
            instance.put(name,new Family(name));
        }
        return instance.get(name);
    }

    private Family(String name){
        familyName=name;
        haveLoaded=false;
    }
}
```
 &emsp;&emsp;What's more, these three classes all have a variable called haveLoaded whose purpose is to avoid getting data and analyzing data repeatedly. After the data has been loaded, when the data is needed next time it won't be loaded again.  <br>
  *The following code comes from FamiyItem.java*:<br>
 ```java
  private void setFamilyView() {
        showProgress("页面加载中");//开始加载动画
        if (!family.haveLoaded) {
            family.haveLoaded = true;
            new Thread() {
                public void run() {
                    try {
                        detail = GetData.getXML("http://asoiaf.huijiwiki.com/api.php?action=query&format=xml&formatversion=2&list=categorymembers&cmtitle=Category:" + family.familyName + "家族&cmlimit=500");
                        DataAnalysis.GetTop10(detail, family);
                        for (int i = 0; i < 16; ++i) {
                            String name = family.top10Person.get(i).title;
                            picDetail = GetData.getXML("http://asoiaf.huijiwiki.com/api.php?action=query&format=xml&formatversion=2&prop=pageimages&titles=" + name + "&pithumbsize=500");
                            DataAnalysis.pageImgAnalysis(picDetail, name, family);
                        }
                        handler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        family.haveLoaded = false;
                    }
                };
            }.start();
        }
        else{
            handler.sendEmptyMessage(1);
        }
    }
```
<br>

- Display <br>
&emsp;&emsp;In android studio, in order to display a page, the coding  includes two parts. The first part is the xml files, which is used to express the layout, but also, we can write the static  information directly on the xml file. The second part is the java file used to control the element in the xml file dynamically.<br> 
&emsp;&emsp;In my project, I used some encapsulated components provided by android studio, such as ListView, GridView and ExpandableListView. In this kind of views, android developed some related adapter classes. When developers need to control the items in a certain viw dynamically, we can create a subclass inheriting a corresponding adapter class and implement the methods.<br>
 *The following code comes from MyAdapter.java*: (It's an adapter for the GridView in item_family)<br>

 ```java
  @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView==null) {
            vi = inflater.inflate(R.layout.item_grid_icon, null);
        }
        TextView title = (TextView)vi.findViewById(R.id.txt_icon); // 姓名
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.img_icon); // 图片
        ImageAndText page = new ImageAndText();
        page = data.get(position);
        title.setText(page.getText());
        thumb_image.setImageBitmap(page.getBitmap());
        return vi;
    }
```
<br>

### 2. API usage
- huikiwiki api <br>
&emsp;&emsp;This api provider supply much infomation about "A Song of Ice and Fire" to developers. The data it returns can be in the format of xml. In my project, I let it return xml data and parse them. I mainly use this api provider to complete the ***family page***. <br>
&emsp;&emsp;First, I can get the family members in each family by calling its *categorymembers* api, for example, I can get members in family Stark by calling the following api:
http://asoiaf.huijiwiki.com/api.php?action=query&format=xml&formatversion=2&list=categorymembers&cmtitle=Category:史塔克家族&cmlimit=500
<br>
&emsp;&emsp;The data it returns display in this way:<br>
```xml
<?xml version="1.0"?>
<api batchcomplete="">
    <warnings>
        <main xml:space="preserve">Unrecognized parameter: formatversion.</main>
    </warnings>
    <query>
        <categorymembers>
            <cm pageid="4796" ns="0" title="班扬·史塔克(消歧义)" />
            <cm pageid="170" ns="0" title="布兰登·史塔克(消歧义)" />
            <cm pageid="272" ns="0" title="寒冰" />
            <cm pageid="5024" ns="0" title="瑞卡德·史塔克(消歧义)" />
            <cm pageid="50228" ns="0" title="艾吉娜儿·史塔克" />
            <cm pageid="50231" ns="0" title="艾罗德·史塔克" />
            <cm pageid="50011" ns="0" title="艾莎·史塔克" />
            <cm pageid="5536" ns="0" title="艾德勒·史塔克" />
            <cm pageid="3892" ns="0" title="艾德利克·史塔克" />
            <cm pageid="36629" ns="0" title="艾德瑞克·史塔克" />
            <cm pageid="242" ns="0" title="艾德·史塔克" />
            <cm pageid="3904" ns="0" title="艾德温·史塔克" />
            <cm pageid="48291" ns="0" title="艾拉德·史塔克" />
            <cm pageid="3899" ns="0" title="艾里昂·史塔克" />
            <cm pageid="50283" ns="0" title="艾里克·史塔克" />
            <cm pageid="7185" ns="0" title="埃林" />
            <cm pageid="50227" ns="0" title="艾莉娜·史塔克" />
            <cm pageid="62" ns="0" title="艾莉亚·史塔克" />
            <cm pageid="3898" ns="0" title="艾隆·史塔克" />
            <cm pageid="50222" ns="0" title="艾娜·诺瑞" />
```

&emsp;&emsp;I find that the field *pageid* shows the importance of a character in the family, so I sort the characters according to their *pageid*. In my project, I choose the top16 characters in each family to show on the ***family page***.<br>
&emsp;&emsp;Then, I call the *pageimages* api to get the cover picture url of each major character in a certain family. After parsing the data, I show theie picture and name on the ***family page*** in the form of GridView. For example, I can get Eddard Stark's cover picture by calling this api: http://asoiaf.huijiwiki.com/api.php?action=query&format=xml&formatversion=2&prop=pageimages&titles=艾德·史塔克&pithumbsize=500
<br>
&emsp;&emsp;The data it returns display in this way:<br>
```xml
<?xml version="1.0"?>
<api batchcomplete="">
    <warnings>
        <main xml:space="preserve">Unrecognized parameter: formatversion.</main>
    </warnings>
    <query>
        <pages>
            <page _idx="242" pageid="242" ns="0" title="艾德·史塔克" pageimage="Collage_lb_image_page7_5_1.jpg">
                <thumbnail source="https://huiji-public.huijistatic.com/asoiaf/uploads/3/36/Collage_lb_image_page7_5_1.jpg" width="429" height="330" />
            </page>
        </pages>
    </query>
</api>
```

<br>

- douban api<br>
&emsp;&emsp; Douban api can provide information about the "A Song of Ice and Fire" book series. The data it returns is in the form of json. I mainly use this api provider for the ***book page***. <br>
&emsp;&emsp; Each book has its id in the system of douban api, so once I know the id of a certain book, I can get its information. For example, if I want to get information of book1: "Game of Thrones", I can call this api: https://api.douban.com/v2/book/6964050?apikey=0df993c66c0c636e29ecbb5344252a4a <br>

```json
{
    "rating": {
        "max": 10,
        "numRaters": 6786,
        "average": "9.4",
        "min": 0
    },
    "subtitle": "",
    "author": [
        "[美]乔治·R.R.马丁"
    ],
    "pubdate": "2012-1-1",
    "tags": [
        {
            "count": 1659,
            "name": "冰与火之歌",
            "title": "冰与火之歌"
        },
        {
            "count": 1370,
            "name": "奇幻",
            "title": "奇幻"
        },
        {
            "count": 1004,
            "name": "乔治·马丁",
            "title": "乔治·马丁"
        },
        {
            "count": 870,
            "name": "小说",
            "title": "小说"
        },
        {
            "count": 637,
            "name": "美国",
            "title": "美国"
        },
        {
            "count": 573,
            "name": "史诗",
            "title": "史诗"
        },
        {
            "count": 552,
            "name": "奇幻史诗",
            "title": "奇幻史诗"
        },
        {
            "count": 482,
            "name": "外国文学",
            "title": "外国文学"
        }
    ],
    "origin_title": "A Song of Ice and Fire1 A Game of Thrones",
    "image": "https://img1.doubanio.com/view/subject/m/public/s8484799.jpg",
    "binding": "盒装",
    "translator": [
        "屈畅",
        "谭光磊"
    ],
    "catalog": "",
    "pages": "873",
    "images": {
        "small": "https://img1.doubanio.com/view/subject/s/public/s8484799.jpg",
        "large": "https://img1.doubanio.com/view/subject/l/public/s8484799.jpg",
        "medium": "https://img1.doubanio.com/view/subject/m/public/s8484799.jpg"
    },
    "alt": "https://book.douban.com/subject/6964050/",
    "id": "6964050",
    "publisher": "重庆出版社",
    "isbn10": "7229047218",
    "isbn13": "9787229047214",
    "title": "冰与火之歌·卷一·权力的游戏（全三册）",
    "url": "https://api.douban.com/v2/book/6964050",
    "alt_title": "A Song of Ice and Fire1 A Game of Thrones",
    "author_intro": "乔治·雷蒙·理查·马丁（Geoger Raymond Richard Martin）：\n1948年出生于美国新泽西州的贝约恩。\n在1971年取得硕士学位以后，马丁陆续在重要的科幻杂志上，如《类比》杂志，发表了许多短篇作品，并很快以感伤怀旧的浪漫笔触，略带歌特气息的荒凉氛围，以及糅合恐怖小说元素的科幻作品，受到读者瞩目。\n这一时期他的代表作有中短篇小说《莱安娜之歌》（A Song For Lya），《沙王》（Sandkings），《十字架与龙》（The Way Of Cross And Dragon）等，长篇小说《光逝》（Dying Of The Light），《热夜之梦》（Fever Dream），《末日狂歌》（The Armageddon Rag）等。\n而在此后的十年中，马丁转向影剧界发展，担任了多部电视剧集的编剧工作。\n直到上世纪90年代，马丁受到《冰与火之歌》故事的感召，退出影剧界，转而投入这部著作的创作中。\n马丁二十七岁以小说《莱安娜之歌》摘下象征幻想小说最高成就的雨果奖。此后他不仅在文学上获奖连连，更曾在好莱坞担任编剧长达十年之久。\n至今，他已获四次雨果奖，两次星云奖，一次世界奇幻文学奖，十一次轨迹奖。\n2011年，美国《时代周刊》将马丁评为\"全世界最有影响力的一百位人物\"之一，肯定了乔治·马丁在幻想文坛上的至尊地位。",
    "summary": "在这个四季时序错乱，长时酷暑或又寒冬十年的世界，残酷、黑暗的一系列宫廷斗争，相互厮杀，不会停歇。远在南方的七大王国国王劳勃突然造访北方的临冬城，会见城主、也是同他从小一起长大的挚友——艾德，这不是简单的叙旧，而是想让他担任七大王国的首相。其实在终年冰封的北境，艾德与妻儿也过着与世无争的生活，这次到访无疑让艾德千思万绪，一切的秘密在一封密信中爆发。前首相琼恩?艾林的死因，矛头全指向了劳勃的皇后，第一望族的瑟曦，为了支援劳勃，艾德投入了这一场宫廷的漩涡中。\n战争才半露面，心灵的撞击就如此猛烈，美好才一开始，黑暗就铺天盖地袭来。愈是接近死亡，一切就愈真实，愈能与读者产生呼应。拿起这本书，就无法放下了。",
    "series": {
        "id": "5608",
        "title": "独角兽书系"
    },
    "price": "120.00元"
}
```
&emsp;&emsp; After parsing the data, I get information of a book in terms of its name, author, translators' name, douban score, first release time and book summary.<br><br>

- IceAndFire api <br>
&emsp;&emsp; This api provider provides information about "a Song of Ice and Fire" in terms of characters, books and buildings. Since "A Song of Ice and Fire" book series drive the plot vividly with POV (Point of  View), something valuable in this api is the pov character it provides. I can get pov characters in the book by calling its book api, then display the pov character infomation on the ***book page***. Here's an example, if I want to get the pov characters in book1, I need to call the following api: https://anapioficeandfire.com/api/books/1 ,then it returns the pov character api url.
```json
 "povCharacters": [
        "https://anapioficeandfire.com/api/characters/148",
        "https://anapioficeandfire.com/api/characters/208",
        "https://anapioficeandfire.com/api/characters/232",
        "https://anapioficeandfire.com/api/characters/339",
        "https://anapioficeandfire.com/api/characters/583",
        "https://anapioficeandfire.com/api/characters/957",
        "https://anapioficeandfire.com/api/characters/1052",
        "https://anapioficeandfire.com/api/characters/1109",
        "https://anapioficeandfire.com/api/characters/1303"
    ]
```
By parsing the urls it returns, I can get the names of the pov characters.<br><br>

- cloudMusic api(wangyi cloud music) <br>
&emsp;&emsp;CloudMusic api provider can supply infomation about the music in "The Game of Thrones" TV series. The data it returns is in the format of json. I mainly use this api provider to complete the ***Music page***. <br>
&emsp;&emsp; First, I can get information of each season's album. Every album has its id in the system of CloudMusic api, I can call the coresponding api once I know the id of an album. For example, if I want to get information about the album for season2, I can call this api: https://api.imjad.cn/cloudmusic/?type=album&id=189848, the data it returns tells what songs in the album. The following is the data for one of the song in this album, called "Main Title".
```json
{
            "rtUrls": [],
            "ar": [
                {
                    "id": 41992,
                    "name": "Ramin Djawadi",
                    "tns": [
                        "拉敏贾瓦迪"
                    ]
                }
            ],
            "al": {
                "id": 189848,
                "name": "Game Of Thrones - Season 2 (Music From The HBO®  Series)",
                "picUrl": "https://p2.music.126.net/DSlHfaTRMiAhP7Apux8CQA==/18853325881488160.jpg",
                "tns": [
                    "冰与火之歌：权力的游戏 第二季"
                ],
                "pic_str": "18853325881488160",
                "pic": 18853325881488160
            },
            "st": 3,
            "a": null,
            "m": {
                "br": 160000,
                "fid": 18798350301627195,
                "size": 2145743,
                "vd": 0.388694
            },
            "v": 21,
            "mv": 0,
            "fee": 8,
            "no": 1,
            "alia": [],
            "pop": 85.0,
            "rt": "",
            "mst": 9,
            "cp": 7003,
            "crbt": null,
            "cf": "",
            "dt": 107226,
            "h": {
                "br": 320000,
                "fid": 18727981557454671,
                "size": 4291440,
                "vd": 0.290563
            },
            "l": {
                "br": 96000,
                "fid": 3418381670650958,
                "size": 1287463,
                "vd": 0.119575
            },
            "rtUrl": null,
            "ftype": 0,
            "rtype": 0,
            "rurl": null,
            "pst": 0,
            "t": 0,
            "cd": "1",
            "djId": 0,
            "name": "Main Title",
            "id": 1883170,
            "privilege": {
                "id": 1883170,
                "fee": 8,
                "payed": 0,
                "st": 0,
                "pl": 128000,
                "dl": 0,
                "sp": 7,
                "cp": 1,
                "subp": 1,
                "cs": false,
                "maxbr": 999000,
                "fl": 128000,
                "toast": false,
                "flag": 4,
                "preSell": false
            }
        },
```
&emsp;&emsp; After parsing the data it returns, I can get the id of the song, then I can get the audio resource by calling api using its song id. For example, from the data above, I know the id for "Main Title" is 1883170, so I can get its audio resource by calling this api: https://api.imjad.cn/cloudmusic/?type=song&id=1883170 ,the data it return contains the audio url, so users can listen to the song.<br><br>
