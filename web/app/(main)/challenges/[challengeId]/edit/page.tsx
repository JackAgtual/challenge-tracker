import ChallengeForm from "@/components/ChallengeForm";
import { Button } from "@/components/ui/button";
import { client } from "@/lib/api-client";
import { TChallengeDataFormSchema } from "@/types/form-types";
import Link from "next/link";

export default async function Page({
  params,
}: {
  params: Promise<{ challengeId: string }>;
}) {
  const { challengeId: challengeIdStr } = await params;
  const challengeId = Number(challengeIdStr);

  const res = await client.GET("/challenges/{challengeId}", {
    params: { path: { challengeId } },
  });

  const defaultVals: TChallengeDataFormSchema = res.error
    ? { name: "", durationDays: 0 }
    : {
        name: res.data.challenge.name,
        durationDays: res.data.challenge.durationDays || 0,
      };

  return (
    <div>
      <h1>Edit challenge</h1>
      <ChallengeForm
        action="EDIT"
        challengeId={challengeId}
        defaultValues={defaultVals}
      />
      <Button
        variant="outline"
        render={<Link href={`/challenges/${challengeId}`} />}
        nativeButton={false}
      >
        Cancel
      </Button>
    </div>
  );
}
