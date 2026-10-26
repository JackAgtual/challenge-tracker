import { ChallengeOverviewCard } from "@/components/challenge-overview-card";
import { components } from "@/types/api";
import Link from "next/link";

type Props = {
  title: string;
  data: components["schemas"]["ChallengeResponse"][];
};

export function ChallengeSection({ title, data }: Props) {
  return (
    <div>
      <h2 className="text-lg py-4">{title}:</h2>
      {data.length === 0 ? (
        <p>You don't have any {title.toLocaleLowerCase()} challenges</p>
      ) : (
        <div className="flex flex-wrap gap-6 justify-center md:justify-start">
          {data.map((challenge) => (
            <Link
              key={challenge.id}
              href={`/challenges/${challenge.id}`}
              className="block w-full max-w-2xs"
            >
              <ChallengeOverviewCard
                challenge={challenge}
                canEditAndStartChallenge={false}
              />
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}
