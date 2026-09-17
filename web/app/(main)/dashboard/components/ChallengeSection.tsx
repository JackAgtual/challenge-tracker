import ChallengeOverviewCard from "@/components/ChallengeOverviewCard";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { components } from "@/types/api";
import Link from "next/link";

type ChallengeSectionProps = {
  title: string;
  data: components["schemas"]["ChallengeResponse"][];
};

export default function ChallengeSection({
  title,
  data,
}: ChallengeSectionProps) {
  return (
    <div>
      <h2 className="text-lg">{title}</h2>
      {data.length === 0 ? (
        <p>You don't have any {title.toLocaleLowerCase()} challenges</p>
      ) : (
        <div className="grid grid-cols-1 gap-y-6">
          {data.map((challenge) => (
            <Link
              key={challenge.id}
              href={`/challenges/${challenge.id}`}
              className="max-w-2xs"
            >
              <ChallengeOverviewCard challenge={challenge} />
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}
